package app.scheduler;

import app.model.entity.Order;
import app.model.enums.Status;
import app.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static app.util.LogMessages.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderStatusUpdateScheduler {

    private final OrderService orderService;

    @Scheduled(fixedDelay = 200000)
    public void updateOrderStatuses() {

        LocalDateTime now = LocalDateTime.now();

        // Process PENDING orders older than 24 hours - auto-cancel them
        List<Order> pendingOrders = orderService.getOrdersReadyForStatusUpdate(
                Status.PENDING, now.minusHours(24));

        if (!pendingOrders.isEmpty()) {
            for (Order order : pendingOrders) {
                orderService.updateOrderStatus(order, Status.CANCELED);
                log.info(ORDER_AUTO_CANCELLED, order.getOrderNumber());
            }
            log.info(ORDER_AUTO_CANCELLED_COUNT, pendingOrders.size());
        }

        // Process PAID orders older than 2 hours - move to SHIPPED
        List<Order> paidOrders = orderService.getOrdersReadyForStatusUpdate(
                Status.PAID, now.minusHours(2));

        if (!paidOrders.isEmpty()) {
            for (Order order : paidOrders) {
                orderService.updateOrderStatus(order, Status.SHIPPED);
                log.info(ORDER_AUTO_SHIPPED, order.getOrderNumber());
            }
            log.info(ORDER_AUTO_SHIPPED_COUNT, paidOrders.size());
        }

        // Process SHIPPED orders older than 72 hours - move to COMPLETED
        List<Order> shippedOrders = orderService.getOrdersReadyForStatusUpdate(
                Status.SHIPPED, now.minusHours(72));

        if (!shippedOrders.isEmpty()) {
            for (Order order : shippedOrders) {
                orderService.updateOrderStatus(order, Status.COMPLETED);
                log.info(ORDER_AUTO_COMPLETED, order.getOrderNumber());
            }
            log.info(ORDER_AUTO_COMPLETED_COUNT, shippedOrders.size());
        }

        if (pendingOrders.isEmpty() && paidOrders.isEmpty() && shippedOrders.isEmpty()) {
            log.info(ORDER_NO_STATUS_UPDATES);
        }
    }
}
