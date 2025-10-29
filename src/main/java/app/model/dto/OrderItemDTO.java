package app.model.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO implements Serializable {

    private UUID albumId;
    private String title;
    private String artistFirstName;
    private String artistLastName;
    private String coverUrl;
    private BigDecimal unitPrice;
    private int quantity;

    public BigDecimal getTotalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}

