package app.service;

import app.model.dto.CartItemDTO;
import app.model.dto.CheckoutDTO;
import app.model.entity.Order;
import app.model.entity.User;

import java.util.List;

public interface OrderService {

    Order createOrder(CheckoutDTO checkoutDTO, List<CartItemDTO> cartItems, User user);

    List<Order> getAllOrders();

    Order getOrderByOrderNumber(String orderNumber);
}
