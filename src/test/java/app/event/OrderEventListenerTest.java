package app.event;

import app.event.payload.OrderCreatedEvent;
import app.notification.services.NotificationService;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.UUID;

import static app.util.SuccessMessages.NOTIFICATION_ORDER_CONFIRMATION;
import static app.util.SuccessMessages.ORDER_CONFIRMATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class OrderEventListenerTest {

    private NotificationService notificationService;
    private OrderEventListener listener;

    private Logger logger;
    private ListAppender<ch.qos.logback.classic.spi.ILoggingEvent> listAppender;

    @BeforeEach
    void setup() {

        notificationService = mock(NotificationService.class);
        listener = new OrderEventListener(notificationService);

        logger = (Logger) LoggerFactory.getLogger(OrderEventListener.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
        logger.setLevel(Level.INFO);
    }

    @AfterEach
    void tearDown() {

        if (logger != null && listAppender != null) {
            logger.detachAppender(listAppender);
            listAppender.stop();
        }
    }

    @Test
    void handleOrderCreated_sendsNotificationWithExpectedSubjectAndBody_andLogsInfo() {

        UUID userId = UUID.randomUUID();
        String username = "john";
        String orderNumber = "ORD-123";
        BigDecimal totalAmount = new BigDecimal("42.50");

        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .userId(userId)
                .username(username)
                .orderNumber(orderNumber)
                .totalAmount(totalAmount)
                .build();

        listener.handleOrderCreated(event);

        ArgumentCaptor<UUID> userIdCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);

        verify(notificationService, times(1))
                .sendNotification(userIdCaptor.capture(), subjectCaptor.capture(), bodyCaptor.capture());

        assertThat(userIdCaptor.getValue()).isEqualTo(userId);
        assertThat(subjectCaptor.getValue()).isEqualTo(ORDER_CONFIRMATION);
        assertThat(bodyCaptor.getValue())
                .isEqualTo(String.format(NOTIFICATION_ORDER_CONFIRMATION, username, orderNumber, totalAmount));

        String combinedLogs = listAppender.list.stream()
                .map(ILoggingEvent::getFormattedMessage)
                .reduce("", (a, b) -> a + "\n" + b);

        assertThat(combinedLogs).contains("Sending confirmation notification for order " + orderNumber);
    }
}
