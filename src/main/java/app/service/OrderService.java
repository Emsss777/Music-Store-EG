package app.service;

import app.model.dto.CartItemDTO;
import app.model.dto.CheckoutDTO;
import app.model.entity.Order;
import app.model.entity.User;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {

    Order createOrder(CheckoutDTO checkoutDTO, List<CartItemDTO> cartItems, User user);

    Order getOrderByOrderNumber(String orderNumber);

    List<Order> getOrdersByUser(User user);

    int getTotalAlbumsPurchasedByUser(User user);

    BigDecimal getTotalAmountSpentByUser(User user);

    List<Order> getAllOrders();
}
