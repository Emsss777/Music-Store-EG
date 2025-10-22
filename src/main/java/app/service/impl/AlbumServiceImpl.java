package app.service.impl;

import app.exception.DomainException;
import app.model.entity.Album;
import app.model.enums.PrimaryGenre;
import app.repository.AlbumRepo;
import app.service.AlbumService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static app.util.ExceptionMessages.ALBUM_DOES_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepo albumRepo;

    @Override
    public Album getAlbumByAlbumTitle(String albumTitle) {

        return albumRepo.findByTitle(albumTitle).orElseThrow(() ->
                new DomainException(ALBUM_DOES_NOT_EXIST.formatted(albumTitle)));
    }

    @Override
    public void saveAlbum(Album albumEntity) {

        albumRepo.save(albumEntity);
    }

    @Override
    public List<Album> getAlbums(@Nullable PrimaryGenre genre) {

        return genre != null
                ? albumRepo.findByGenreOrderByYearDesc(genre)
                : albumRepo.findAllByOrderByYearDesc();
    }

    @Override
    public Album getAlbumById(UUID id) {

        return albumRepo.findById(id).orElseThrow(() ->
                new DomainException(ALBUM_DOES_NOT_EXIST.formatted(id)));
    }
}
