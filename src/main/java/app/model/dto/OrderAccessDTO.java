package app.model.dto;

import app.validation.OrderOwner;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

import static app.util.ExceptionMessages.ORDER_ID_CANNOT_BE_NULL;

@Builder
@Getter
@Setter
@OrderOwner
@AllArgsConstructor
@NoArgsConstructor
public class OrderAccessDTO {

    @NotNull(message = ORDER_ID_CANNOT_BE_NULL)
    private UUID orderId;
}
