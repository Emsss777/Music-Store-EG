package app.service;

import app.model.entity.AlbumEntity;
import app.model.enums.PrimaryGenre;

import java.util.List;

public interface AlbumService {

    AlbumEntity getAlbumByAlbumTitle(String albumTitle);

    void saveAlbum(AlbumEntity albumEntity);

    List<AlbumEntity> getAllAlbums();

    List<AlbumEntity> getAlbumsByGenre(PrimaryGenre genre);
}
