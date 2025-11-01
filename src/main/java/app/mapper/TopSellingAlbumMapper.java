package app.mapper;

import app.model.dto.TopSellingAlbumDTO;
import app.model.projection.TopSellingAlbumProjection;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TopSellingAlbumMapper {

    public static TopSellingAlbumDTO toDTO(TopSellingAlbumProjection projection) {

        if (projection == null) return null;

        return TopSellingAlbumDTO.builder()
                .albumId(projection.getAlbumId())
                .title(projection.getTitle())
                .artist(projection.getArtist())
                .unitsSold(projection.getUnitsSold())
                .revenue(projection.getRevenue())
                .coverUrl(projection.getCoverUrl())
                .build();
    }
}

