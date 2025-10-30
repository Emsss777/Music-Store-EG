package app.service.impl;

import app.exception.DomainException;
import app.model.dto.CartItemDTO;
import app.model.dto.CheckoutDTO;
import app.model.dto.OrderItemDTO;
import app.model.entity.*;
import app.model.enums.PaymentMethod;
import app.model.enums.Status;
import app.repository.OrderItemRepo;
import app.repository.OrderRepo;
import app.service.AlbumService;
import app.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static app.util.ExceptionMessages.ORDER_DOES_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final AlbumService albumService;

    @Override
    @Transactional
    public Order createOrder(CheckoutDTO checkoutDTO, List<CartItemDTO> cartItems, User user) {

        BigDecimal totalAmount = cartItems.stream()
                .map(CartItemDTO::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .orderNumber(generateOrderNumber())
                .status(Status.PENDING)
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .totalAmount(totalAmount)
                .owner(user)
                .createdOn(LocalDateTime.now())
                .build();

        Order savedOrder = orderRepo.save(order);

        for (CartItemDTO cartItem : cartItems) {
            Album album = albumService.getAlbumById(cartItem.getAlbumId());

            Optional<OrderItem> existingItem = orderItemRepo.findByOrderAndAlbum(savedOrder, album);
            if (existingItem.isPresent()) {
                OrderItem item = existingItem.get();
                item.setQuantity(item.getQuantity() + cartItem.getQuantity());
                orderItemRepo.save(item);
            } else {
                OrderItem orderItem = OrderItem.builder()
                        .album(album)
                        .order(savedOrder)
                        .unitPrice(cartItem.getPrice())
                        .quantity(cartItem.getQuantity())
                        .build();
                orderItemRepo.save(orderItem);
            }
        }

        return savedOrder;
    }

    @Override
    public Order getOrderByOrderNumber(String orderNumber) {

        return orderRepo.findByOrderNumber(orderNumber).orElseThrow(() ->
                new DomainException(ORDER_DOES_NOT_EXIST + orderNumber));
    }

    @Override
    public List<OrderItemDTO> getOrderItems(UUID orderId) {

        Order order = getOrderById(orderId);

        return order.getItems().stream()
                .map(item -> OrderItemDTO.builder()
                        .albumId(item.getAlbum().getId())
                        .title(item.getAlbum().getTitle())
                        .artistFirstName(item.getAlbum().getArtist().getFirstName())
                        .artistLastName(item.getAlbum().getArtist().getLastName())
                        .coverUrl(item.getAlbum().getCoverUrl())
                        .unitPrice(item.getUnitPrice())
                        .quantity(item.getQuantity())
                        .build())
                .toList();
    }

    @Override
    public List<Order> getOrdersByUser(User user) {

        return orderRepo.findByOwner(user);
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

    private Order getOrderById(UUID orderId) {

        return orderRepo.findById(orderId).orElseThrow(() ->
                new DomainException(ORDER_DOES_NOT_EXIST + orderId));
    }


    private String generateOrderNumber() {

        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
