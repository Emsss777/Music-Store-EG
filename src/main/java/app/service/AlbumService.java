package app.service;

import app.model.dto.SaveAlbumDTO;
import app.model.entity.Album;
import app.model.enums.PrimaryGenre;

import java.util.List;
import java.util.UUID;

public interface AlbumService {

    Album getAlbumByAlbumTitle(String albumTitle);

    void saveAlbum(Album album);

    List<Album> getAllAlbums(PrimaryGenre genre);

    Album getAlbumById(UUID albumId);

    void saveAlbumFromDTO(SaveAlbumDTO saveAlbumDTO);
}
