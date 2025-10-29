package app.model.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartSummaryDTO implements Serializable {

    private List<CartItemDTO> items;
    private BigDecimal total;
    private int itemCount;

    public static CartSummaryDTO of(List<CartItemDTO> items) {

        BigDecimal total = items.stream()
                .map(CartItemDTO::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int count = items.stream()
                .mapToInt(CartItemDTO::getQuantity)
                .sum();

        return CartSummaryDTO.builder()
                .items(items)
                .total(total)
                .itemCount(count)
                .build();
    }
}
