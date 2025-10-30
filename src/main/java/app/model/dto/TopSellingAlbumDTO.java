package app.model.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopSellingAlbumDTO {

    UUID albumId;
    String title;
    String artist;
    Long unitsSold;
    BigDecimal revenue;
    String coverUrl;
}
