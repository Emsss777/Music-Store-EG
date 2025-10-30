package app.model.projection;

import java.math.BigDecimal;
import java.util.UUID;

public interface TopSellingAlbumProjection {

    UUID getAlbumId();
    String getTitle();
    String getArtist();
    Long getUnitsSold();
    BigDecimal getRevenue();
    String getCoverUrl();
}
