package app.service.impl;

import app.exception.DomainException;
import app.model.dto.SaveAlbumDTO;
import app.model.entity.Album;
import app.model.entity.Artist;
import app.model.enums.PrimaryGenre;
import app.repository.AlbumRepo;
import app.service.AlbumService;
import app.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static app.util.ExceptionMessages.ALBUM_DOES_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepo albumRepo;
    private final ArtistService artistService;

    @Override
    public Album getAlbumByAlbumTitle(String albumTitle) {

        return albumRepo.findByTitle(albumTitle).orElseThrow(() ->
                new DomainException(ALBUM_DOES_NOT_EXIST.formatted(albumTitle)));
    }

    @Override
    public void saveAlbum(Album album) {

        albumRepo.save(album);
    }

    @Override
    public List<Album> getAllAlbums(PrimaryGenre genre) {

        return genre != null
                ? albumRepo.findByGenreOrderByYearDesc(genre)
                : albumRepo.findAllByOrderByYearDesc();
    }

    @Override
    public Album getAlbumById(UUID albumId) {

        return albumRepo.findById(albumId).orElseThrow(() ->
                new DomainException(ALBUM_DOES_NOT_EXIST.formatted(albumId)));
    }

    @Override
    public void saveAlbumFromDTO(SaveAlbumDTO saveAlbumDTO) {

        Artist artist = artistService.getArtistById(saveAlbumDTO.getArtistId());

        Album album = Album.builder()
                .title(saveAlbumDTO.getTitle())
                .genre(saveAlbumDTO.getGenre())
                .year(saveAlbumDTO.getYear())
                .description(saveAlbumDTO.getDescription())
                .coverUrl(saveAlbumDTO.getCoverUrl())
                .price(saveAlbumDTO.getPrice())
                .artist(artist)
                .build();

        albumRepo.save(album);
    }
}
