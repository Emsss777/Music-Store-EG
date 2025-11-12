package app.service.impl;

import app.exception.DomainException;
import app.export.ExporterPDF;
import app.mapper.OrderItemMapper;
import app.mapper.OrderMapper;
import app.model.dto.CartItemDTO;
import app.model.dto.CheckoutDTO;
import app.model.entity.Album;
import app.model.entity.Order;
import app.model.entity.OrderItem;
import app.model.entity.User;
import app.model.enums.PaymentMethod;
import app.model.enums.Status;
import app.notification.services.NotificationService;
import app.repository.OrderItemRepo;
import app.repository.OrderRepo;
import app.service.AlbumService;
import app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static app.TestDataFactory.*;
import static app.util.SuccessMessages.ORDER_CONFIRMATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepo orderRepo;
    @Mock
    private OrderItemRepo orderItemRepo;
    @Mock
    private AlbumService albumService;
    @Mock
    private NotificationService notificationService;
    @Mock
    private UserService userService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User user;
    private Album album;

    @BeforeEach
    void setUp() {

        user = aUser();
        user.setId(UUID.randomUUID());
        album = anAlbum(anArtist());
        album.setId(UUID.randomUUID());
    }

    @Test
    void createOrder_withNewCartItems_shouldPersistOrderItemsAndNotifyUser() {

        CheckoutDTO checkoutDTO = aCheckoutDTO();
        CartItemDTO firstItem = aCartItem(album);
        firstItem.setQuantity(2);
        CartItemDTO secondItem = aCartItem(album);
        secondItem.setAlbumId(UUID.randomUUID());
        secondItem.setQuantity(1);
        Album secondAlbum = anAlbum(anArtist());
        secondAlbum.setId(secondItem.getAlbumId());

        when(orderRepo.save(any(Order.class))).thenAnswer(invocation -> {
            Order orderToSave = invocation.getArgument(0, Order.class);
            orderToSave.setId(UUID.randomUUID());
            orderToSave.setOrderNumber("ORD-12345678");
            orderToSave.setCreatedOn(LocalDateTime.now());
            return orderToSave;
        });
        when(albumService.getAlbumById(firstItem.getAlbumId())).thenReturn(album);
        when(albumService.getAlbumById(secondItem.getAlbumId())).thenReturn(secondAlbum);
        when(orderItemRepo.findByOrderAndAlbum(any(), any())).thenReturn(Optional.empty());

        Order savedOrder = orderService.createOrder(checkoutDTO, List.of(firstItem, secondItem), user);

        assertThat(savedOrder.getOwner()).isEqualTo(user);
        assertThat(savedOrder.getTotalAmount()).isEqualByComparingTo(new BigDecimal("59.97"));
        verify(orderRepo, times(1)).save(any(Order.class));
        verify(orderItemRepo, times(2)).save(any(OrderItem.class));
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(notificationService, times(1))
                .sendNotification(eq(user.getId()), eq(ORDER_CONFIRMATION), messageCaptor.capture());
        String notificationBody = messageCaptor.getValue();
        assertThat(notificationBody).contains(savedOrder.getId().toString());
        assertThat(notificationBody).contains("59.97");
    }

    @Test
    void createOrder_whenOrderItemAlreadyExists_shouldIncreaseQuantity() {

        CheckoutDTO checkoutDTO = aCheckoutDTO();
        CartItemDTO cartItem = aCartItem(album);
        cartItem.setQuantity(3);

        when(orderRepo.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(albumService.getAlbumById(cartItem.getAlbumId())).thenReturn(album);
        Order existingOrder = OrderMapper.fromCheckoutDTO(checkoutDTO, user, cartItem.getTotalPrice());
        existingOrder.setId(UUID.randomUUID());
        OrderItem existingOrderItem = OrderItemMapper.fromCartItem(cartItem, existingOrder, album);
        existingOrderItem.setQuantity(1);

        when(orderItemRepo.findByOrderAndAlbum(any(Order.class), eq(album)))
                .thenReturn(Optional.of(existingOrderItem));

        orderService.createOrder(checkoutDTO, List.of(cartItem), user);

        assertThat(existingOrderItem.getQuantity()).isEqualTo(4);
        verify(orderItemRepo, times(1)).save(existingOrderItem);
    }

    @Test
    void getOrderById_whenMissing_shouldThrowDomainException() {

        UUID orderId = UUID.randomUUID();
        when(orderRepo.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> orderService.getOrderById(orderId));
    }

    @Test
    void exportToPDF_shouldReturnBytesFromExporter() {

        UUID userId = UUID.randomUUID();
        Order order = Order.builder()
                .id(UUID.randomUUID())
                .orderNumber("ORD-TEST")
                .status(Status.COMPLETED)
                .owner(user)
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .totalAmount(BigDecimal.TEN)
                .createdOn(LocalDateTime.now())
                .build();
        when(userService.getUserById(userId)).thenReturn(user);
        when(orderRepo.findByOwner(user)).thenReturn(List.of(order));

        byte[] expectedBytes = "pdf-content".getBytes();
        try (MockedStatic<ExporterPDF> exporter = Mockito.mockStatic(ExporterPDF.class)) {
            exporter.when(() -> ExporterPDF.exportOrders(List.of(order))).thenReturn(expectedBytes);

            byte[] result = orderService.exportToPDF(userId);

            assertThat(result).isEqualTo(expectedBytes);
            exporter.verify(() -> ExporterPDF.exportOrders(List.of(order)));
        }
    }
}