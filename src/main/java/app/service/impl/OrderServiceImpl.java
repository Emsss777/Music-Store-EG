package app.service.impl;

import app.exception.DomainException;
import app.mapper.OrderItemMapper;
import app.mapper.OrderMapper;
import app.model.dto.CartItemDTO;
import app.model.dto.CheckoutDTO;
import app.model.entity.*;
import app.notification.services.NotificationService;
import app.repository.OrderItemRepo;
import app.repository.OrderRepo;
import app.service.AlbumService;
import app.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static app.util.ExceptionMessages.ORDER_DOES_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final AlbumService albumService;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public Order createOrder(CheckoutDTO checkoutDTO, List<CartItemDTO> cartItems, User user) {

        BigDecimal totalAmount = cartItems.stream()
                .map(CartItemDTO::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = OrderMapper.fromCheckoutDTO(checkoutDTO, user, totalAmount);
        Order savedOrder = orderRepo.save(order);

        for (CartItemDTO cartItem : cartItems) {
            Album album = albumService.getAlbumById(cartItem.getAlbumId());

            orderItemRepo.findByOrderAndAlbum(savedOrder, album)
                    .ifPresentOrElse(
                            item -> {
                                item.setQuantity(item.getQuantity() + cartItem.getQuantity());
                                orderItemRepo.save(item);
                            },
                            () -> orderItemRepo.save(OrderItemMapper.fromCartItem(cartItem, savedOrder, album))
                    );
        }

        notificationService.sendNotification(
                user.getId(),
                "Order Confirmation",
                "Dear %s, your order #%s has been successfully created! Total amount: %s"
                        .formatted(user.getUsername(), savedOrder.getId(), totalAmount)
        );

        return savedOrder;
    }

    @Override
    public Order getOrderByOrderNumber(String orderNumber) {

        return orderRepo.findByOrderNumber(orderNumber).orElseThrow(() ->
                new DomainException(ORDER_DOES_NOT_EXIST + orderNumber));
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
}
