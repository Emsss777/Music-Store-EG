package app.service.impl;

import app.exception.DomainException;
import app.model.entity.AlbumEntity;
import app.model.enums.PrimaryGenre;
import app.repository.AlbumRepo;
import app.service.AlbumService;
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
    public AlbumEntity getAlbumByAlbumTitle(String albumTitle) {

        return albumRepo.findByTitle(albumTitle).orElseThrow(() ->
                new DomainException(ALBUM_DOES_NOT_EXIST.formatted(albumTitle)));
    }

    @Override
    public void saveAlbum(AlbumEntity albumEntity) {

        albumRepo.save(albumEntity);
    }

    @Override
    public List<AlbumEntity> getAllAlbums() {

        return albumRepo.findAllByOrderByYearDesc();
    }

    @Override
    public List<AlbumEntity> getAlbumsByGenre(PrimaryGenre genre) {

        return albumRepo.findByGenreOrderByYearDesc(genre);
    }

    @Override
    public AlbumEntity getAlbumById(UUID id) {

        return albumRepo.findById(id).orElseThrow(() ->
                new DomainException(ALBUM_DOES_NOT_EXIST.formatted(id)));
    }
}
