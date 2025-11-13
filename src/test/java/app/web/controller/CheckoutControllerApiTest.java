package app.web.controller;

import app.config.WebMvcConfig;
import app.model.dto.CartItemDTO;
import app.model.entity.Order;
import app.model.entity.User;
import app.model.enums.PaymentMethod;
import app.model.enums.Status;
import app.security.AuthenticationMetadata;
import app.service.CartService;
import app.service.OrderService;
import app.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static app.TestDataFactory.aCartItem;
import static app.TestDataFactory.anAlbum;
import static app.TestDataFactory.anArtist;
import static app.TestDataFactory.aUser;
import static app.util.FlashAttributes.FLASH_ERROR;
import static app.util.FlashAttributes.FLASH_MESSAGE;
import static app.util.FlashAttributes.FLASH_ORDER_NUMBER;
import static app.util.SuccessMessages.ORDER_PLACED_SUCCESS;
import static app.util.UrlPaths.URL_CHECKOUT;
import static app.util.UrlPaths.URL_ORDER_NUMBER;
import static app.util.Views.VIEW_CHECKOUT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(WebMvcConfig.class)
@WebMvcTest(CheckoutController.class)
class CheckoutControllerApiTest {

    @MockitoBean
    private CartService cartService;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getCheckoutPage_whenCartEmpty_shouldRedirectToCart() throws Exception {

        given(cartService.getCartItems(any())).willReturn(null);

        MockHttpServletRequestBuilder request = get(URL_CHECKOUT)
                .with(user(authenticatedUser()));

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));
    }

    @Test
    void getCheckoutPage_withItems_shouldReturnCheckoutView() throws Exception {

        CartItemDTO cartItem = aCartItem(anAlbum(anArtist()));
        cartItem.setAlbumId(UUID.randomUUID());
        List<CartItemDTO> items = List.of(cartItem);
        BigDecimal total = new BigDecimal("19.99");
        given(cartService.getCartItems(any())).willReturn(items);
        given(cartService.getCartItemCount(any())).willReturn(1);
        given(cartService.getCartTotal(any())).willReturn(total);

        MockHttpServletRequestBuilder request = get(URL_CHECKOUT)
                .with(user(authenticatedUser()));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_CHECKOUT))
                .andExpect(model().attribute("cartItems", items))
                .andExpect(model().attribute("cartTotal", total))
                .andExpect(model().attribute("itemCount", 1))
                .andExpect(model().attributeExists("checkoutDTO"));
    }

    @Test
    void processCheckout_whenCartEmpty_shouldRedirectBackWithError() throws Exception {

        given(cartService.getCartItems(any())).willReturn(List.of());

        MockHttpServletRequestBuilder request = post(URL_CHECKOUT)
                .with(user(authenticatedUser()))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"))
                .andExpect(flash().attributeExists(FLASH_ERROR));
    }

    @Test
    void processCheckout_happyPath_shouldCreateOrderClearCartAndRedirect() throws Exception {

        MockHttpSession session = new MockHttpSession();
        CartItemDTO cartItem = aCartItem(anAlbum(anArtist()));
        cartItem.setAlbumId(UUID.randomUUID());
        User user = aUser();
        user.setId(UUID.randomUUID());
        Order order = Order.builder()
                .id(UUID.randomUUID())
                .orderNumber("ORD-12345678")
                .status(Status.PENDING)
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .totalAmount(new BigDecimal("19.99"))
                .owner(user)
                .build();

        given(cartService.getCartItems(any())).willReturn(List.of(cartItem));
        given(userService.getUserById(user.getId())).willReturn(user);
        given(orderService.createOrder(any(), anyList(), eq(user))).willReturn(order);

        MockHttpServletRequestBuilder request = post(URL_CHECKOUT)
                .session(session)
                .with(user(authenticatedUser(user.getId())))
                .with(csrf())
                .param("firstName", "Quality")
                .param("lastName", "Assurance")
                .param("email", "qa@example.com");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/made-order" + URL_ORDER_NUMBER + order.getOrderNumber()))
                .andExpect(flash().attribute(FLASH_MESSAGE, ORDER_PLACED_SUCCESS + order.getOrderNumber()))
                .andExpect(flash().attribute(FLASH_ORDER_NUMBER, order.getOrderNumber()));

        verify(cartService, times(1)).clearCart(any());
    }

    private AuthenticationMetadata authenticatedUser() {

        return authenticatedUser(UUID.randomUUID());
    }

    private AuthenticationMetadata authenticatedUser(UUID userId) {

        return AuthenticationMetadata.builder()
                .userId(userId)
                .username("qa-user")
                .password("password")
                .role(app.model.enums.UserRole.USER)
                .isActive(true)
                .build();
    }
}