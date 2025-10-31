package app.mapper;

import app.model.dto.OrderDTO;
import app.model.dto.OrderItemDetailDTO;
import app.model.entity.Order;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class OrderMapper {

    public static OrderDTO toDTO(Order order) {

        if (order == null) {
            return null;
        }

        List<OrderItemDetailDTO> itemDTOs = order.getItems() != null
                ? order.getItems().stream()
                .map(item -> OrderItemDetailDTO.builder()
                        .id(item.getId())
                        .unitPrice(item.getUnitPrice())
                        .quantity(item.getQuantity())
                        .album(AlbumMapper.toDTO(item.getAlbum()))
                        .build())
                .collect(Collectors.toList())
                : List.of();

        return OrderDTO.builder()
                .id(order.getId())
                .status(order.getStatus())
                .orderNumber(order.getOrderNumber())
                .createdOn(order.getCreatedOn())
                .paymentMethod(order.getPaymentMethod())
                .totalAmount(order.getTotalAmount())
                .items(itemDTOs)
                .owner(UserMapper.toBasicDTO(order.getOwner()))
                .build();
    }

    public static List<OrderDTO> toDTOList(List<Order> orders) {

        if (orders == null) {
            return List.of();
        }

        return orders.stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }
}