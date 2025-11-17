package app.service.impl;

import app.event.payload.OrderCreatedEvent;
import app.exception.DomainException;
import app.export.ExporterPDF;
import app.mapper.OrderItemMapper;
import app.mapper.OrderMapper;
import app.model.dto.CartItemDTO;
import app.model.dto.CheckoutDTO;
import app.model.dto.OrderDTO;
import app.model.entity.*;
import app.model.enums.Status;
import app.repository.OrderRepo;
import app.service.AlbumService;
import app.service.OrderService;
import app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static app.util.ExceptionMessages.ORDER_DOES_NOT_EXIST;
import static app.util.LogMessages.ORDER_CREATED;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final AlbumService albumService;
    private final UserService userService;
    private final ApplicationEventPublisher publisher;

    @Override
    @Transactional
    public OrderDTO createOrder(CheckoutDTO checkoutDTO, List<CartItemDTO> cartItems, User user) {

        BigDecimal totalAmount = cartItems.stream()
                .map(CartItemDTO::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = OrderMapper.fromCheckoutDTO(checkoutDTO, user, totalAmount);

        for (CartItemDTO cartItem : cartItems) {
            Album album = albumService.getAlbumById(cartItem.getAlbumId());

            OrderItem existingItem = order.getItems().stream()
                    .filter(i -> i.getAlbum().getId().equals(album.getId()))
                    .findFirst()
                    .orElse(null);

            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + cartItem.getQuantity());
            } else {
                OrderItem newItem = OrderItemMapper.fromCartItem(cartItem, order, album);
                order.getItems().add(newItem);
            }
        }

        Order savedOrder = orderRepo.save(order);

        publisher.publishEvent(new OrderCreatedEvent(
                user.getId(),
                user.getUsername(),
                savedOrder.getOrderNumber(),
                savedOrder.getTotalAmount()
        ));

        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_GREEN, ORDER_CREATED),
                savedOrder.getOrderNumber(), user.getUsername(), totalAmount, savedOrder.getItems().size());

        return OrderMapper.toDTO(savedOrder);
    }

    @Override
    public Order getOrderById(UUID orderId) {

        return orderRepo.findById(orderId).orElseThrow(() ->
                new DomainException(ORDER_DOES_NOT_EXIST + orderId));
    }

    @Override
    public Order getOrderByOrderNumber(String orderNumber) {

        return orderRepo.findByOrderNumber(orderNumber).orElseThrow(() ->
                new DomainException(ORDER_DOES_NOT_EXIST + orderNumber));
    }

    @Override
    public OrderDTO getOrderByOrderNumberDTO(String orderNumber) {

        Order order = this.getOrderByOrderNumber(orderNumber);
        return OrderMapper.toDTO(order);
    }

    @Override
    public List<Order> getOrdersByUser(User user) {

        return orderRepo.findByOwner(user);
    }

    @Override
    public List<OrderDTO> getOrdersByUserDTO(User user) {

        List<Order> orders = this.getOrdersByUser(user);
        return OrderMapper.toDTOList(orders);
    }

    @Override
    public int getTotalAlbumsPurchasedByUser(User user) {

        List<Order> userOrders = orderRepo.findByOwner(user);

        return userOrders.stream()
                .flatMap(order -> order.getItems().stream())
                .mapToInt(OrderItem::getQuantity)
                .sum();
    }

    @Override
    public BigDecimal getTotalAmountSpentByUser(User user) {

        List<Order> userOrders = orderRepo.findByOwner(user);

        return userOrders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<Order> getAllOrders() {

        return orderRepo.findAll(Sort.by(Sort.Direction.DESC, "createdOn"));
    }

    @Override
    public List<OrderDTO> getAllOrdersDTO() {

        List<Order> orders = this.getAllOrders();
        return OrderMapper.toDTOList(orders);
    }

    @Override
    public List<Order> getOrdersReadyForStatusUpdate(Status status, LocalDateTime beforeDateTime) {

        return orderRepo.findByStatusAndCreatedOnBefore(status, beforeDateTime);
    }

    @Override
    @Transactional
    public void updateOrderStatus(Order order, Status newStatus) {

        order.setStatus(newStatus);
        orderRepo.save(order);
    }

    @Override
    public byte[] exportToPDF(UUID userId) {

        User currentUser = userService.getUserById(userId);
        List<Order> orders = orderRepo.findByOwner(currentUser);

        return ExporterPDF.exportOrders(orders);
    }
}
