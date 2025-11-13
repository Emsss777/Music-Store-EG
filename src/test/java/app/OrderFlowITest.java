package app;

import app.event.UserRegisteredEventProducer;
import app.event.UserUpdatedEventProducer;
import app.model.dto.CartItemDTO;
import app.model.dto.CheckoutDTO;
import app.model.dto.OrderDTO;
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
import static org.assertj.core.api.Assertions.assertThat;

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

        Artist artist = anArtist();
        artistRepo.save(artist);
        entityManager.flush();

        Album album1 = anAlbum(artist);
        Album album2 = anAlbum(artist);
        album2.setTitle("Test Album 2");
        albumRepo.save(album1);
        albumRepo.save(album2);
        entityManager.flush();

        User user = aUser();
        userRepo.save(user);
        entityManager.flush();

        MockHttpSession session = new MockHttpSession();

        cartService.addToCart(session, album1.getId());
        cartService.addToCart(session, album1.getId());
        cartService.addToCart(session, album2.getId());

        List<CartItemDTO> cartItems = cartService.getCartItems(session);
        assertThat(cartItems).hasSize(2);
        assertThat(cartItems.stream()
                .filter(item -> item.getAlbumId().equals(album1.getId()))
                .findFirst()
                .orElseThrow()
                .getQuantity()).isEqualTo(2);
        assertThat(cartItems.stream()
                .filter(item -> item.getAlbumId().equals(album2.getId()))
                .findFirst()
                .orElseThrow()
                .getQuantity()).isEqualTo(1);

        CheckoutDTO checkoutDTO = aCheckoutDTO();

        long ordersCountBefore = orderRepo.count();
        long orderItemsCountBefore = orderItemRepo.count();

        OrderDTO orderDTO = orderService.createOrder(checkoutDTO, cartItems, user);

        cartService.clearCart(session);

        entityManager.flush();
        entityManager.clear();

        assertThat(orderDTO).isNotNull();
        assertThat(orderDTO.getOrderNumber()).isNotNull();
        assertThat(orderDTO.getItems()).hasSize(2);
        assertThat(orderDTO.getTotalAmount()).isPositive();

        assertThat(orderRepo.count()).isEqualTo(ordersCountBefore + 1);
        assertThat(orderItemRepo.count()).isEqualTo(orderItemsCountBefore + 2);

        Optional<Order> savedOrderOpt = orderRepo.findByOrderNumber(orderDTO.getOrderNumber());
        assertThat(savedOrderOpt).isPresent();

        Order savedOrder = savedOrderOpt.get();
        assertThat(savedOrder.getOwner().getId()).isEqualTo(user.getId());
        assertThat(savedOrder.getTotalAmount()).isEqualTo(orderDTO.getTotalAmount());
        assertThat(savedOrder.getItems()).hasSize(2);

        OrderItem item1 = savedOrder.getItems().stream()
                .filter(item -> item.getAlbum().getId().equals(album1.getId()))
                .findFirst()
                .orElseThrow();
        assertThat(item1.getQuantity()).isEqualTo(2);
        assertThat(item1.getUnitPrice()).isEqualTo(album1.getPrice());

        OrderItem item2 = savedOrder.getItems().stream()
                .filter(item -> item.getAlbum().getId().equals(album2.getId()))
                .findFirst()
                .orElseThrow();
        assertThat(item2.getQuantity()).isEqualTo(1);
        assertThat(item2.getUnitPrice()).isEqualTo(album2.getPrice());

        List<CartItemDTO> cartItemsAfter = cartService.getCartItems(session);
        assertThat(cartItemsAfter).isEmpty();
    }
}
