package app.service;

import app.model.dto.CartItemDTO;
import app.model.dto.CheckoutDTO;
import app.model.entity.Order;
import app.model.entity.User;
import app.model.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderService {

    Order createOrder(CheckoutDTO checkoutDTO, List<CartItemDTO> cartItems, User user);

    Order getOrderById(UUID orderId);

    Order getOrderByOrderNumber(String orderNumber);

    List<Order> getOrdersByUser(User user);

    int getTotalAlbumsPurchasedByUser(User user);

    BigDecimal getTotalAmountSpentByUser(User user);

    List<Order> getAllOrders();

    List<Order> getOrdersReadyForStatusUpdate(Status status, LocalDateTime beforeDateTime);

    void updateOrderStatus(Order order, Status newStatus);

    byte[] exportToPDF(UUID userId);
}
