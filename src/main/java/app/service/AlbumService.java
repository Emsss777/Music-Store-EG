package app.service;

import app.model.dto.AlbumDTO;
import app.model.dto.SaveAlbumDTO;
import app.model.entity.Album;
import app.model.enums.PrimaryGenre;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface AlbumService {

    void getAlbumByAlbumTitle(String albumTitle);

    void saveAlbum(Album album);

    List<Album> getAllAlbums(PrimaryGenre genre);

    List<Album> getAllAlbums();

    List<Album> getRandomAlbums(int limit);

    List<AlbumDTO> getRandomAlbumsDTO(int limit);

    Album getAlbumById(UUID albumId);

    void saveAlbumFromDTO(SaveAlbumDTO saveAlbumDTO);

    void deleteAlbum(UUID albumId);

    long getTotalAlbumCount();

    long getAlbumsAddedThisMonth();

    BigDecimal getAverageAlbumPrice();
}
