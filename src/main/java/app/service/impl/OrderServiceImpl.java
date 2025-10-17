package app.service.impl;

import app.model.dto.CartItemDTO;
import app.model.dto.CheckoutDTO;
import app.model.entity.*;
import app.model.enums.PaymentMethod;
import app.model.enums.Status;
import app.repository.OrderItemRepo;
import app.repository.OrderRepo;
import app.service.AlbumService;
import app.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final AlbumService albumService;

    @Override
    @Transactional
    public OrderEntity createOrder(CheckoutDTO checkoutDTO, List<CartItemDTO> cartItems, UserEntity user) {

        BigDecimal totalAmount = cartItems.stream()
                .map(CartItemDTO::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        OrderEntity order = OrderEntity.builder()
                .orderNumber(generateOrderNumber())
                .status(Status.PENDING)
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .totalAmount(totalAmount)
                .owner(user)
                .createdOn(LocalDateTime.now())
                .build();

        OrderEntity savedOrder = orderRepo.save(order);

        for (CartItemDTO cartItem : cartItems) {
            AlbumEntity album = albumService.getAlbumById(cartItem.getAlbumId());

            OrderItemEntity orderItem = OrderItemEntity.builder()
                    .album(album)
                    .order(savedOrder)
                    .unitPrice(cartItem.getPrice())
                    .build();

            orderItemRepo.save(orderItem);
        }

        return savedOrder;
    }

    private String generateOrderNumber() {

        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
