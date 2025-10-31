package app.mapper;

import app.model.dto.OrderDTO;
import app.model.dto.OrderItemDetailDTO;
import app.model.entity.Order;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
}