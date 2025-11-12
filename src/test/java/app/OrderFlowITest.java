package app;

import app.event.UserRegisteredEventProducer;
import app.event.UserUpdatedEventProducer;
import app.model.dto.CartItemDTO;
import app.model.dto.CheckoutDTO;
import app.model.entity.*;
import app.notification.services.NotificationService;
import app.repository.AlbumRepo;
import app.repository.ArtistRepo;
import app.repository.OrderItemRepo;
import app.repository.OrderRepo;
import app.repository.UserRepo;
import app.service.CartService;
import app.service.OrderService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static app.TestDataFactory.*;
import static app.util.SuccessMessages.ORDER_CONFIRMATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class OrderFlowITest {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ArtistRepo artistRepo;

    @Autowired
    private AlbumRepo albumRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private EntityManager entityManager;

    @MockitoBean
    private NotificationService notificationService;

    @MockitoBean
    private UserRegisteredEventProducer userRegisteredEventProducer;

    @MockitoBean
    private UserUpdatedEventProducer userUpdatedEventProducer;

    @Test
    @Transactional
    void placeOrder_happyPath_shouldPersistOrderAndOrderItems() {

        // Given
        Artist artist = artistRepo.save(anArtist());
        Album album = albumRepo.save(anAlbum(artist));
        User user = userRepo.save(aUser());

        MockHttpSession session = new MockHttpSession();
        cartService.addToCart(session, album.getId());
        List<CartItemDTO> cartItems = cartService.getCartItems(session);
        CheckoutDTO checkoutDTO = aCheckoutDTO();

        // When
        Order order = orderService.createOrder(checkoutDTO, cartItems, user);

        entityManager.flush();
        entityManager.clear();

        // Then
        Order actualOrder = orderRepo.findById(order.getId()).orElseThrow();
        assertThat(actualOrder.getTotalAmount()).isEqualByComparingTo(cartItems.get(0).getTotalPrice());

        Optional<OrderItem> orderItemOpt = orderItemRepo.findByOrderAndAlbum(actualOrder, album);
        assertThat(orderItemOpt).isPresent();

        OrderItem orderItem = orderItemOpt.get();
        assertThat(orderItem.getQuantity()).isEqualTo(cartItems.get(0).getQuantity());
        assertThat(orderItem.getUnitPrice()).isEqualByComparingTo(cartItems.get(0).getTotalPrice());

        verify(notificationService).sendNotification(
                eq(user.getId()),
                eq(ORDER_CONFIRMATION),
                contains(order.getId().toString())
        );
    }
}
