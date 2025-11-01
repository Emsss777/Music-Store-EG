package app.mapper;

import app.model.dto.CartItemDTO;
import app.model.entity.Album;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CartItemMapper {

    public static CartItemDTO fromAlbum(Album album) {

        if (album == null) return null;

        return CartItemDTO.builder()
                .albumId(album.getId())
                .title(album.getTitle())
                .artistName(album.getArtist().getArtistName())
                .coverUrl(album.getCoverUrl())
                .price(album.getPrice())
                .quantity(1)
                .build();
    }
}
