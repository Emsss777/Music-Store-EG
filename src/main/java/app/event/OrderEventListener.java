package app.event;

import app.event.payload.OrderCreatedEvent;
import app.notification.services.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

import static app.util.LogMessages.NOTIFICATION_ORDER_CONFIRMATION_SENDING;
import static app.util.SuccessMessages.NOTIFICATION_ORDER_CONFIRMATION;
import static app.util.SuccessMessages.ORDER_CONFIRMATION;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventListener {

    private final NotificationService notificationService;

    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {

        UUID userId = event.getUserId();
        String username = event.getUsername();
        String orderNumber = event.getOrderNumber();
        BigDecimal totalAmount = event.getTotalAmount();

        String message = String.format(NOTIFICATION_ORDER_CONFIRMATION,
                username, orderNumber, totalAmount);

        log.info(AnsiOutput.toString(
                AnsiColor.BRIGHT_GREEN, NOTIFICATION_ORDER_CONFIRMATION_SENDING), orderNumber);
        notificationService.sendNotification(userId, ORDER_CONFIRMATION, message);
    }
}
