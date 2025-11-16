package app.scheduler;

import app.model.entity.Order;
import app.model.entity.User;
import app.model.enums.PaymentMethod;
import app.model.enums.Status;
import app.repository.OrderRepo;
import app.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static app.TestDataFactory.aUser;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class OrderStatusUpdateSchedulerITest {

    @Autowired
    private OrderStatusUpdateScheduler scheduler;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private UserRepo userRepo;

    @Test
    void updateOrderStatuses_updatesEligibleOrdersAndLeavesOthersUntouched() {

        User owner = userRepo.save(aUser());

        LocalDateTime now = LocalDateTime.now();

        // Eligible for auto-cancel (PENDING older than 24h)
        Order pendingOld = orderRepo.save(Order.builder()
                .status(Status.PENDING)
                .orderNumber("ORD-" + UUID.randomUUID())
                .createdOn(now.minusHours(25))
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .totalAmount(new BigDecimal("10.00"))
                .owner(owner)
                .build());

        // Not eligible (PENDING newer than 24h)
        Order pendingNew = orderRepo.save(Order.builder()
                .status(Status.PENDING)
                .orderNumber("ORD-" + UUID.randomUUID())
                .createdOn(now.minusHours(5))
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .totalAmount(new BigDecimal("20.00"))
                .owner(owner)
                .build());

        // Eligible for auto-ship (PAID older than 2h)
        Order paidOld = orderRepo.save(Order.builder()
                .status(Status.PAID)
                .orderNumber("ORD-" + UUID.randomUUID())
                .createdOn(now.minusHours(3))
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .totalAmount(new BigDecimal("30.00"))
                .owner(owner)
                .build());

        // Not eligible (PAID newer than 2h)
        Order paidNew = orderRepo.save(Order.builder()
                .status(Status.PAID)
                .orderNumber("ORD-" + UUID.randomUUID())
                .createdOn(now.minusMinutes(45))
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .totalAmount(new BigDecimal("40.00"))
                .owner(owner)
                .build());

        // Eligible for auto-complete (SHIPPED older than 72h)
        Order shippedOld = orderRepo.save(Order.builder()
                .status(Status.SHIPPED)
                .orderNumber("ORD-" + UUID.randomUUID())
                .createdOn(now.minusHours(80))
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .totalAmount(new BigDecimal("50.00"))
                .owner(owner)
                .build());

        // Not eligible (SHIPPED newer than 72h)
        Order shippedNew = orderRepo.save(Order.builder()
                .status(Status.SHIPPED)
                .orderNumber("ORD-" + UUID.randomUUID())
                .createdOn(now.minusHours(10))
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .totalAmount(new BigDecimal("60.00"))
                .owner(owner)
                .build());

        // Execute scheduler logic directly
        scheduler.updateOrderStatuses();

        // Refresh from DB
        pendingOld = orderRepo.findById(pendingOld.getId()).orElseThrow();
        pendingNew = orderRepo.findById(pendingNew.getId()).orElseThrow();
        paidOld = orderRepo.findById(paidOld.getId()).orElseThrow();
        paidNew = orderRepo.findById(paidNew.getId()).orElseThrow();
        shippedOld = orderRepo.findById(shippedOld.getId()).orElseThrow();
        shippedNew = orderRepo.findById(shippedNew.getId()).orElseThrow();

        // Assertions
        assertThat(pendingOld.getStatus()).isEqualTo(Status.CANCELED);
        assertThat(pendingNew.getStatus()).isEqualTo(Status.PENDING);

        assertThat(paidOld.getStatus()).isEqualTo(Status.SHIPPED);
        assertThat(paidNew.getStatus()).isEqualTo(Status.PAID);

        assertThat(shippedOld.getStatus()).isEqualTo(Status.COMPLETED);
        assertThat(shippedNew.getStatus()).isEqualTo(Status.SHIPPED);
    }
}
