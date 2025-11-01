package app.mapper;

import app.model.dto.CheckoutDTO;
import app.model.dto.OrderDTO;
import app.model.dto.OrderItemDetailDTO;
import app.model.entity.Order;
import app.model.entity.User;
import app.model.enums.PaymentMethod;
import app.model.enums.Status;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@UtilityClass
public class OrderMapper {

    public static OrderDTO toDTO(Order order) {

        if (order == null) return null;

        List<OrderItemDetailDTO> itemDTOs = Optional.ofNullable(order.getItems())
                .orElse(Collections.emptyList())
                .stream()
                .map(item -> OrderItemDetailDTO.builder()
                        .id(item.getId())
                        .unitPrice(item.getUnitPrice())
                        .quantity(item.getQuantity())
                        .album(AlbumMapper.toDTO(item.getAlbum()))
                        .build())
                .toList();

        return OrderDTO.builder()
                .id(order.getId())
                .status(order.getStatus())
                .orderNumber(order.getOrderNumber())
                .createdOn(order.getCreatedOn())
                .paymentMethod(order.getPaymentMethod())
                .totalAmount(order.getTotalAmount())
                .items(itemDTOs)
                .owner(order.getOwner() != null
                        ? UserMapper.toBasicDTO(order.getOwner())
                        : null)
                .build();
    }

    public static List<OrderDTO> toDTOList(List<Order> orders) {

        if (orders == null) return Collections.emptyList();

        return orders.stream()
                .map(OrderMapper::toDTO)
                .toList();
    }

    public static Order fromCheckoutDTO(CheckoutDTO checkout, User user, BigDecimal totalAmount) {

        if (checkout == null || user == null) return null;

        return Order.builder()
                .orderNumber(generateOrderNumber())
                .status(Status.PENDING)
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .totalAmount(totalAmount)
                .owner(user)
                .createdOn(LocalDateTime.now())
                .build();
    }

    private String generateOrderNumber() {

        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}