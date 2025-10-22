package app.service;

import app.model.entity.AlbumEntity;
import app.model.enums.PrimaryGenre;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.UUID;

public interface AlbumService {

    AlbumEntity getAlbumByAlbumTitle(String albumTitle);

    void saveAlbum(AlbumEntity albumEntity);

    List<AlbumEntity> getAlbums(@Nullable PrimaryGenre genre);

    AlbumEntity getAlbumById(UUID id);
}
