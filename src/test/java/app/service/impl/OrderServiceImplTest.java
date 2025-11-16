package app.service.impl;

import app.event.payload.OrderCreatedEvent;
import app.exception.DomainException;
import app.export.ExporterPDF;
import app.mapper.OrderMapper;
import app.model.dto.CartItemDTO;
import app.model.dto.CheckoutDTO;
import app.model.dto.OrderDTO;
import app.model.entity.Album;
import app.model.entity.Order;
import app.model.entity.OrderItem;
import app.model.entity.User;
import app.model.enums.PaymentMethod;
import app.model.enums.Status;
import app.repository.OrderRepo;
import app.service.AlbumService;
import app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static app.TestDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private AlbumService albumService;

    @Mock
    private UserService userService;

    @Mock
    private ApplicationEventPublisher publisher;

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
        CartItemDTO cartItem1 = aCartItem(album);
        cartItem1.setQuantity(2);
        CartItemDTO cartItem2 = aCartItem(anAlbum(anArtist()));
        cartItem2.setAlbumId(UUID.randomUUID());
        cartItem2.setQuantity(1);

        List<CartItemDTO> cartItems = List.of(cartItem1, cartItem2);
        Album album2 = anAlbum(anArtist());
        album2.setId(cartItem2.getAlbumId());

        when(albumService.getAlbumById(cartItem1.getAlbumId())).thenReturn(album);
        when(albumService.getAlbumById(cartItem2.getAlbumId())).thenReturn(album2);

        Order savedOrder = Order.builder()
                .id(UUID.randomUUID())
                .orderNumber("ORD-TEST123")
                .status(Status.PENDING)
                .owner(user)
                .totalAmount(cartItem1.getTotalPrice().add(cartItem2.getTotalPrice()))
                .items(new ArrayList<>())
                .createdOn(LocalDateTime.now())
                .build();

        try (MockedStatic<OrderMapper> orderMapperMock = Mockito.mockStatic(OrderMapper.class)) {
            Order newOrder = Order.builder()
                    .orderNumber("ORD-TEMP")
                    .status(Status.PENDING)
                    .owner(user)
                    .totalAmount(cartItem1.getTotalPrice().add(cartItem2.getTotalPrice()))
                    .items(new ArrayList<>())
                    .createdOn(LocalDateTime.now())
                    .build();

            orderMapperMock.when(() -> OrderMapper.fromCheckoutDTO(any(CheckoutDTO.class), eq(user), any(BigDecimal.class)))
                    .thenReturn(newOrder);

            when(orderRepo.save(any(Order.class))).thenAnswer(invocation -> {
                Order order = invocation.getArgument(0);
                order.setId(savedOrder.getId());
                order.setOrderNumber(savedOrder.getOrderNumber());
                return order;
            });

            orderMapperMock.when(() -> OrderMapper.toDTO(any(Order.class))).thenAnswer(invocation -> {
                Order order = invocation.getArgument(0);
                return OrderDTO.builder()
                        .id(order.getId())
                        .orderNumber(order.getOrderNumber())
                        .status(order.getStatus())
                        .totalAmount(order.getTotalAmount())
                        .createdOn(order.getCreatedOn())
                        .items(order.getItems().stream()
                                .map(item -> app.model.dto.OrderItemDetailDTO.builder()
                                        .id(item.getId())
                                        .unitPrice(item.getUnitPrice())
                                        .quantity(item.getQuantity())
                                        .build())
                                .toList())
                        .build();
            });

            OrderDTO result = orderService.createOrder(checkoutDTO, cartItems, user);

            ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
            verify(orderRepo, times(1)).save(orderCaptor.capture());

            Order capturedOrder = orderCaptor.getValue();
            assertThat(capturedOrder.getItems()).hasSize(2);
            assertThat(capturedOrder.getItems()).extracting(OrderItem::getQuantity)
                    .containsExactly(2, 1);
            assertThat(capturedOrder.getOwner()).isEqualTo(user);
            assertThat(capturedOrder.getTotalAmount()).isEqualTo(
                    cartItem1.getTotalPrice().add(cartItem2.getTotalPrice()));

            ArgumentCaptor<OrderCreatedEvent> eventCaptor = ArgumentCaptor.forClass(OrderCreatedEvent.class);
            verify(publisher, times(1)).publishEvent(eventCaptor.capture());

            OrderCreatedEvent capturedEvent = eventCaptor.getValue();
            assertThat(capturedEvent.getUserId()).isEqualTo(user.getId());
            assertThat(capturedEvent.getUsername()).isEqualTo(user.getUsername());
            assertThat(capturedEvent.getOrderNumber()).isNotNull();
            assertThat(capturedEvent.getTotalAmount()).isEqualTo(capturedOrder.getTotalAmount());

            assertThat(result).isNotNull();
            assertThat(result.getItems()).hasSize(2);
        }
    }

    @Test
    void createOrder_whenOrderItemAlreadyExists_shouldIncreaseQuantity() {

        CheckoutDTO checkoutDTO = aCheckoutDTO();

        CartItemDTO cartItem1 = aCartItem(album);
        cartItem1.setQuantity(3);
        CartItemDTO cartItem2 = aCartItem(album);
        cartItem2.setQuantity(2);

        List<CartItemDTO> cartItems = List.of(cartItem1, cartItem2);

        when(albumService.getAlbumById(album.getId())).thenReturn(album);

        Order savedOrder = Order.builder()
                .id(UUID.randomUUID())
                .orderNumber("ORD-TEST456")
                .status(Status.PENDING)
                .owner(user)
                .totalAmount(BigDecimal.ZERO)
                .items(new ArrayList<>())
                .createdOn(LocalDateTime.now())
                .build();

        try (MockedStatic<OrderMapper> orderMapperMock = Mockito.mockStatic(OrderMapper.class)) {
            Order newOrder = Order.builder()
                    .orderNumber("ORD-TEMP")
                    .status(Status.PENDING)
                    .owner(user)
                    .totalAmount(BigDecimal.ZERO)
                    .items(new ArrayList<>())
                    .createdOn(LocalDateTime.now())
                    .build();

            orderMapperMock.when(() -> OrderMapper.fromCheckoutDTO(any(CheckoutDTO.class), eq(user), any(BigDecimal.class)))
                    .thenReturn(newOrder);

            when(orderRepo.save(any(Order.class))).thenAnswer(invocation -> {
                Order order = invocation.getArgument(0);
                order.setId(savedOrder.getId());
                order.setOrderNumber(savedOrder.getOrderNumber());

                if (order.getItems() != null && !order.getItems().isEmpty()) {
                    OrderItem item = order.getItems().get(0);
                    BigDecimal newTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                    order.setTotalAmount(newTotal);
                }

                return order;
            });

            orderMapperMock.when(() -> OrderMapper.toDTO(any(Order.class))).thenAnswer(invocation -> {
                Order order = invocation.getArgument(0);
                return OrderDTO.builder()
                        .id(order.getId())
                        .orderNumber(order.getOrderNumber())
                        .status(order.getStatus())
                        .totalAmount(order.getTotalAmount())
                        .createdOn(order.getCreatedOn())
                        .items(order.getItems() != null ? order.getItems().stream()
                                .map(item -> app.model.dto.OrderItemDetailDTO.builder()
                                        .id(item.getId())
                                        .unitPrice(item.getUnitPrice())
                                        .quantity(item.getQuantity())
                                        .build())
                                .toList() : List.of())
                        .build();
            });

            OrderDTO result = orderService.createOrder(checkoutDTO, cartItems, user);

            ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
            verify(orderRepo, times(1)).save(orderCaptor.capture());

            Order capturedOrder = orderCaptor.getValue();
            assertThat(capturedOrder.getItems()).hasSize(1);

            OrderItem finalItem = capturedOrder.getItems().get(0);
            assertThat(finalItem.getQuantity()).isEqualTo(5);
            assertThat(finalItem.getAlbum().getId()).isEqualTo(album.getId());
            assertThat(finalItem.getUnitPrice()).isEqualTo(album.getPrice());

            ArgumentCaptor<OrderCreatedEvent> eventCaptor = ArgumentCaptor.forClass(OrderCreatedEvent.class);
            verify(publisher, times(1)).publishEvent(eventCaptor.capture());

            assertThat(result).isNotNull();
            assertThat(result.getItems()).hasSize(1);
            assertThat(result.getItems().get(0).getQuantity()).isEqualTo(5);
        }
    }

    @Test
    void getOrderById_whenPresent_shouldReturnOrder() {

        UUID orderId = UUID.randomUUID();
        Order order = Order.builder()
                .id(orderId)
                .orderNumber("ORD-OK")
                .status(Status.PENDING)
                .owner(user)
                .totalAmount(BigDecimal.ONE)
                .createdOn(LocalDateTime.now())
                .build();

        when(orderRepo.findById(orderId)).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById(orderId);

        assertThat(result).isEqualTo(order);
    }

    @Test
    void getOrderById_whenMissing_shouldThrowDomainException() {

        UUID orderId = UUID.randomUUID();
        when(orderRepo.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> orderService.getOrderById(orderId));
    }

    @Test
    void getOrderByOrderNumber_whenPresent_shouldReturnOrder() {

        String orderNumber = "ORD-123";
        Order order = Order.builder()
                .id(UUID.randomUUID())
                .orderNumber(orderNumber)
                .status(Status.PENDING)
                .owner(user)
                .totalAmount(BigDecimal.TEN)
                .createdOn(LocalDateTime.now())
                .build();

        when(orderRepo.findByOrderNumber(orderNumber)).thenReturn(Optional.of(order));

        Order result = orderService.getOrderByOrderNumber(orderNumber);
        assertThat(result).isEqualTo(order);
    }

    @Test
    void getOrderByOrderNumber_whenMissing_shouldThrowDomainException() {

        String orderNumber = "ORD-MISSING";
        when(orderRepo.findByOrderNumber(orderNumber)).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> orderService.getOrderByOrderNumber(orderNumber));
    }

    @Test
    void getOrdersByUser_shouldReturnListFromRepo() {

        Order order1 = Order.builder()
                .id(UUID.randomUUID())
                .orderNumber("ORD-1")
                .status(Status.PENDING)
                .owner(user)
                .totalAmount(BigDecimal.ONE)
                .createdOn(LocalDateTime.now())
                .build();
        Order order2 = Order.builder()
                .id(UUID.randomUUID())
                .orderNumber("ORD-2")
                .status(Status.COMPLETED)
                .owner(user)
                .totalAmount(BigDecimal.TEN)
                .createdOn(LocalDateTime.now())
                .build();

        when(orderRepo.findByOwner(user)).thenReturn(List.of(order1, order2));

        List<Order> result = orderService.getOrdersByUser(user);
        assertThat(result).containsExactly(order1, order2);
    }

    @Test
    void getTotalAlbumsPurchasedByUser_shouldSumQuantities() {

        Order order1 = Order.builder()
                .id(UUID.randomUUID())
                .orderNumber("ORD-A")
                .status(Status.PENDING)
                .owner(user)
                .totalAmount(BigDecimal.ZERO)
                .createdOn(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();
        Order order2 = Order.builder()
                .id(UUID.randomUUID())
                .orderNumber("ORD-B")
                .status(Status.PENDING)
                .owner(user)
                .totalAmount(BigDecimal.ZERO)
                .createdOn(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();

        order1.getItems().add(OrderItem.builder().order(order1).album(album).unitPrice(BigDecimal.ONE).quantity(2).build());
        order1.getItems().add(OrderItem.builder().order(order1).album(album).unitPrice(BigDecimal.ONE).quantity(3).build());
        order2.getItems().add(OrderItem.builder().order(order2).album(album).unitPrice(BigDecimal.ONE).quantity(5).build());

        when(orderRepo.findByOwner(user)).thenReturn(List.of(order1, order2));

        int total = orderService.getTotalAlbumsPurchasedByUser(user);
        assertThat(total).isEqualTo(10);
    }

    @Test
    void getTotalAmountSpentByUser_shouldSumTotals() {

        Order order1 = Order.builder()
                .id(UUID.randomUUID())
                .orderNumber("ORD-A")
                .status(Status.PENDING)
                .owner(user)
                .totalAmount(new BigDecimal("12.50"))
                .createdOn(LocalDateTime.now())
                .build();
        Order order2 = Order.builder()
                .id(UUID.randomUUID())
                .orderNumber("ORD-B")
                .status(Status.PENDING)
                .owner(user)
                .totalAmount(new BigDecimal("7.50"))
                .createdOn(LocalDateTime.now())
                .build();

        when(orderRepo.findByOwner(user)).thenReturn(List.of(order1, order2));

        BigDecimal total = orderService.getTotalAmountSpentByUser(user);
        assertThat(total).isEqualByComparingTo(new BigDecimal("20.00"));
    }

    @Test
    void getAllOrders_shouldRequestSortedByCreatedOnDesc() {

        List<Order> orders = List.of(
                Order.builder().id(UUID.randomUUID()).orderNumber("ORD-1").status(Status.PENDING).owner(user).totalAmount(BigDecimal.ONE).createdOn(LocalDateTime.now()).build(),
                Order.builder().id(UUID.randomUUID()).orderNumber("ORD-2").status(Status.COMPLETED).owner(user).totalAmount(BigDecimal.valueOf(2)).createdOn(LocalDateTime.now()).build()
        );

        when(orderRepo.findAll(any(Sort.class))).thenReturn(orders);

        List<Order> result = orderService.getAllOrders();

        assertThat(result).isEqualTo(orders);

        ArgumentCaptor<Sort> sortCaptor = ArgumentCaptor.forClass(Sort.class);
        verify(orderRepo).findAll(sortCaptor.capture());
        Sort sort = sortCaptor.getValue();
        assertThat(sort).isEqualTo(Sort.by(Sort.Direction.DESC, "createdOn"));
    }

    @Test
    void getOrdersReadyForStatusUpdate_shouldDelegateToRepo() {

        LocalDateTime before = LocalDateTime.now().minusDays(1);
        Status status = Status.PENDING;
        List<Order> expected = List.of(
                Order.builder().id(UUID.randomUUID()).orderNumber("ORD-X").status(status).owner(user).totalAmount(BigDecimal.ONE).createdOn(before.minusHours(1)).build()
        );

        when(orderRepo.findByStatusAndCreatedOnBefore(status, before)).thenReturn(expected);

        List<Order> result = orderService.getOrdersReadyForStatusUpdate(status, before);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void updateOrderStatus_shouldSetStatusAndPersist() {

        Order order = Order.builder()
                .id(UUID.randomUUID())
                .orderNumber("ORD-U")
                .status(Status.PENDING)
                .owner(user)
                .totalAmount(BigDecimal.ZERO)
                .createdOn(LocalDateTime.now())
                .build();

        orderService.updateOrderStatus(order, Status.COMPLETED);

        assertThat(order.getStatus()).isEqualTo(Status.COMPLETED);
        verify(orderRepo).save(order);
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
