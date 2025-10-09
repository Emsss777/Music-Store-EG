package app.service;

import app.model.entity.AlbumEntity;

public interface AlbumService {

    AlbumEntity getAlbumByAlbumTitle(String albumTitle);

    void saveAlbum(AlbumEntity albumEntity);
}
