package app.service;

import app.model.dto.CartItemDTO;
import app.model.dto.CheckoutDTO;
import app.model.dto.OrderItemDTO;
import app.model.entity.Order;
import app.model.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface OrderService {

    Order createOrder(CheckoutDTO checkoutDTO, List<CartItemDTO> cartItems, User user);

    Order getOrderByOrderNumber(String orderNumber);

    List<OrderItemDTO> getOrderItems(UUID orderId);

    List<Order> getOrdersByUser(User user);

    int getTotalAlbumsPurchasedByUser(User user);

    BigDecimal getTotalAmountSpentByUser(User user);
}
