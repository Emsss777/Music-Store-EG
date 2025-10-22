package app.service;

import app.model.entity.Album;
import app.model.enums.PrimaryGenre;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.UUID;

public interface AlbumService {

    Album getAlbumByAlbumTitle(String albumTitle);

    void saveAlbum(Album albumEntity);

    List<Album> getAlbums(@Nullable PrimaryGenre genre);

    Album getAlbumById(UUID id);
}
