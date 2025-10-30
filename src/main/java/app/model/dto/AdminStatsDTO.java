package app.model.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminStatsDTO {

    long totalAlbums;
    BigDecimal totalRevenue;
    long activeUsers;
    long ordersToday;
    long allOrders;
    List<TopSellingAlbumDTO> topSellingAlbums;
}
