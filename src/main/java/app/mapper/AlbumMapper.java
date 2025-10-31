package app.mapper;

import app.model.dto.AlbumDTO;
import app.model.entity.Album;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class AlbumMapper {

    public static AlbumDTO toDTO(Album album) {

        if (album == null) {
            return null;
        }

        return AlbumDTO.builder()
                .id(album.getId())
                .title(album.getTitle())
                .genre(album.getGenre())
                .year(album.getYear())
                .description(album.getDescription())
                .coverUrl(album.getCoverUrl())
                .price(album.getPrice())
                .artist(ArtistMapper.toDTO(album.getArtist()))
                .createdOn(album.getCreatedOn())
                .build();
    }

    public static List<AlbumDTO> toDTOList(List<Album> albums) {

        if (albums == null) {
            return List.of();
        }

        return albums.stream()
                .map(AlbumMapper::toDTO)
                .collect(Collectors.toList());
    }
}
