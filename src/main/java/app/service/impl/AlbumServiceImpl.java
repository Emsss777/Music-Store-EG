package app.service.impl;

import app.exception.DomainException;
import app.exception.TitleAlreadyExistException;
import app.model.dto.SaveAlbumDTO;
import app.model.entity.Album;
import app.model.entity.Artist;
import app.model.enums.PrimaryGenre;
import app.repository.AlbumRepo;
import app.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static app.util.ExceptionMessages.*;
import static app.util.SuccessMessages.ALBUM_DELETED_WITH_DETAILS;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepo albumRepo;
    private final ArtistService artistService;
    private final OrderItemManagerService orderItemManager;

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

        Optional<Album> existingAlbum = albumRepo.findByTitle(saveAlbumDTO.getTitle());
        if (existingAlbum.isPresent()) {
            throw new TitleAlreadyExistException(
                    ALBUM_ALREADY_EXISTS.formatted(saveAlbumDTO.getTitle()));
        }

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

    @Override
    public void deleteAlbum(UUID albumId) {

        Album album = getAlbumById(albumId);

        int deletedItems = orderItemManager.deleteAllItemsByAlbumId(albumId);
        int deletedOrders = orderItemManager.deleteEmptyOrders();

        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_GREEN, ALBUM_DELETED_WITH_DETAILS),
                album.getTitle(), deletedItems, deletedOrders);

        albumRepo.delete(album);
    }
}
