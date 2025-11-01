package app.mapper;

import app.model.dto.CartItemDTO;
import app.model.entity.Album;
import app.model.entity.Order;
import app.model.entity.OrderItem;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderItemMapper {

    public static OrderItem fromCartItem(CartItemDTO cartItem, Order order, Album album) {

        if (cartItem == null || order == null || album == null) return null;

        return OrderItem.builder()
                .album(album)
                .order(order)
                .unitPrice(cartItem.getPrice())
                .quantity(cartItem.getQuantity())
                .build();
    }
}
