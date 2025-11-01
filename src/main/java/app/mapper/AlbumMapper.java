package app.mapper;

import app.model.dto.AlbumDTO;
import app.model.dto.SaveAlbumDTO;
import app.model.entity.Album;
import app.model.entity.Artist;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
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

    public static Album fromSaveDTO(SaveAlbumDTO dto, Artist artist) {

        if (dto == null || artist == null) return null;

        return Album.builder()
                .title(dto.getTitle())
                .genre(dto.getGenre())
                .year(dto.getYear())
                .description(Optional.ofNullable(dto.getDescription()).orElse(""))
                .coverUrl(dto.getCoverUrl())
                .price(dto.getPrice())
                .artist(artist)
                .createdOn(LocalDateTime.now())
                .build();
    }

}
