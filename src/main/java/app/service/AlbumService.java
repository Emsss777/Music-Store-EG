package app.service;

import app.model.entity.AlbumEntity;

import java.util.List;

public interface AlbumService {

    AlbumEntity getAlbumByAlbumTitle(String albumTitle);

    void saveAlbum(AlbumEntity albumEntity);

    List<AlbumEntity> getAllAlbums();
}
