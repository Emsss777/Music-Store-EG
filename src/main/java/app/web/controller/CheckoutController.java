package app.web.controller;

import app.model.dto.CartItemDTO;
import app.model.dto.CheckoutDTO;
import app.model.entity.Order;
import app.model.entity.User;
import app.security.AuthenticationMetadata;
import app.service.CartService;
import app.service.OrderService;
import app.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

import static app.util.ExceptionMessages.CART_IS_EMPTY;
import static app.util.FlashAttributes.*;
import static app.util.ModelAttributes.*;
import static app.util.Redirects.REDIRECT_CART;
import static app.util.SuccessMessages.ORDER_PLACED_SUCCESS;
import static app.util.UrlPaths.URL_CHECKOUT;
import static app.util.Views.VIEW_CHECKOUT;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class CheckoutController {

    private final CartService cartService;
    private final OrderService orderService;
    private final UserService userService;

    @GetMapping(URL_CHECKOUT)
    public ModelAndView getCheckoutPage(HttpSession session) {

        List<CartItemDTO> cartItems = cartService.getCartItems(session);

        if (cartItems == null || cartItems.isEmpty()) {

            return new ModelAndView(REDIRECT_CART);
        }

        int itemCount = cartService.getCartItemCount(session);
        BigDecimal cartTotal = cartService.getCartTotal(session);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_CHECKOUT);
        modelAndView.addObject(MODEL_PAGE, VIEW_CHECKOUT);
        modelAndView.addObject(MODEL_CART_ITEMS, cartItems);
        modelAndView.addObject(MODEL_CART_TOTAL, cartTotal);
        modelAndView.addObject(MODEL_ITEM_COUNT, itemCount);
        modelAndView.addObject(MODEL_CHECKOUT_DTO, new CheckoutDTO());

        return modelAndView;
    }

    @PostMapping(URL_CHECKOUT)
    public ModelAndView processCheckout(@ModelAttribute CheckoutDTO checkoutDTO, HttpSession session,
                                  @AuthenticationPrincipal AuthenticationMetadata authMetadata,
                                  RedirectAttributes redirectAttributes) {

        List<CartItemDTO> cartItems = cartService.getCartItems(session);

        if (cartItems == null || cartItems.isEmpty()) {
            redirectAttributes.addFlashAttribute(FLASH_ERROR, CART_IS_EMPTY);

            return new ModelAndView(REDIRECT_CART);
        }

        User user = userService.getUserById(authMetadata.getUserId());

        Order order = orderService.createOrder(checkoutDTO, cartItems, user);

        cartService.clearCart(session);

        redirectAttributes.addFlashAttribute(FLASH_MESSAGE, ORDER_PLACED_SUCCESS + order.getOrderNumber());
        redirectAttributes.addFlashAttribute(FLASH_ORDER_NUMBER, order.getOrderNumber());

        return new ModelAndView(REDIRECT_CART);
    }
}
