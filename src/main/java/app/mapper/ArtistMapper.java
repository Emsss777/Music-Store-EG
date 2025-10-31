package app.mapper;

import app.model.dto.ArtistDTO;
import app.model.entity.Artist;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class ArtistMapper {

    public static ArtistDTO toDTO(Artist artist) {

        if (artist == null) return null;

        return ArtistDTO.builder()
                .id(artist.getId())
                .artistName(artist.getArtistName())
                .stageName(Optional.ofNullable(artist.getStageName()).orElse(""))
                .firstName(artist.getFirstName())
                .lastName(artist.getLastName())
                .artistBio(artist.getArtistBio())
                .primaryGenre(artist.getPrimaryGenre())
                .createdOn(artist.getCreatedOn())
                .build();
    }

    public static List<ArtistDTO> toDTOList(List<Artist> artists) {

        if (artists == null) return Collections.emptyList();

        return artists.stream()
                .map(ArtistMapper::toDTO)
                .toList();
    }
}
