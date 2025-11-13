package app.event.payload;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEvent {

    private UUID userId;
    private String username;
    private String orderNumber;
    private BigDecimal totalAmount;
}
