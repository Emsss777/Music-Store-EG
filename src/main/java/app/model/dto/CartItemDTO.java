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
public class CartItemDTO implements Serializable {

    private UUID albumId;
    private String title;
    private String artistName;
    private String coverUrl;
    private BigDecimal price;
    private int quantity;

    public BigDecimal getTotalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
