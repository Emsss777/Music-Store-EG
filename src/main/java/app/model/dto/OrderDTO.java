package app.model.dto;

import app.model.enums.PaymentMethod;
import app.model.enums.Status;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private UUID id;
    private Status status;
    private String orderNumber;
    private LocalDateTime createdOn;
    private PaymentMethod paymentMethod;
    private BigDecimal totalAmount;

    @Builder.Default
    private List<OrderItemDetailDTO> items = new ArrayList<>();

    private UserBasicDTO owner;
}
