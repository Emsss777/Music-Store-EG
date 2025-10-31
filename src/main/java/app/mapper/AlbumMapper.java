package app.mapper;

import app.model.dto.AlbumDTO;
import app.model.entity.Album;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class AlbumMapper {

    public static AlbumDTO toDTO(Album album) {

        if (album == null) return null;

        return AlbumDTO.builder()
                .id(album.getId())
                .title(album.getTitle())
                .genre(album.getGenre())
                .year(album.getYear())
                .description(Optional.ofNullable(album.getDescription()).orElse(""))
                .coverUrl(album.getCoverUrl())
                .price(album.getPrice())
                .artist(ArtistMapper.toDTO(album.getArtist()))
                .createdOn(album.getCreatedOn())
                .build();
    }

    public static List<AlbumDTO> toDTOList(List<Album> albums) {

        if (albums == null) return Collections.emptyList();

        return albums.stream()
                .map(AlbumMapper::toDTO)
                .toList();
    }
}
