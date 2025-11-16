package app.service.impl;

import app.exception.DomainException;
import app.exception.TitleAlreadyExistException;
import app.model.dto.SaveAlbumDTO;
import app.model.entity.Album;
import app.model.entity.Artist;
import app.model.enums.PrimaryGenre;
import app.repository.AlbumRepo;
import app.service.ArtistService;
import app.service.OrderItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlbumServiceImplTest {

    @Mock
    private AlbumRepo albumRepo;

    @Mock
    private ArtistService artistService;

    @Mock
    private OrderItemService orderItemService;

    @InjectMocks
    private AlbumServiceImpl albumService;

    private Album sampleAlbum;
    private UUID albumId;

    @BeforeEach
    void setUp() {

        albumId = UUID.randomUUID();
        sampleAlbum = Album.builder()
                .title("Test Album")
                .genre(PrimaryGenre.ROCK)
                .year(1999)
                .description("desc")
                .coverUrl("https://img")
                .price(new BigDecimal("9.99"))
                .artist(Artist.builder().artistName("Artist")
                        .primaryGenre(PrimaryGenre.ROCK)
                        .createdOn(LocalDateTime.now())
                        .build())
                .createdOn(LocalDateTime.now())
                .build();
    }

    @Test
    void getAlbumByAlbumTitle_whenFound_doesNotThrow() {

        when(albumRepo.findByTitle("Test")).thenReturn(Optional.of(sampleAlbum));

        assertDoesNotThrow(() -> albumService.getAlbumByAlbumTitle("Test"));
        verify(albumRepo, times(1)).findByTitle("Test");
    }

    @Test
    void getAlbumByAlbumTitle_whenMissing_throwsDomainException() {

        when(albumRepo.findByTitle("Missing")).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> albumService.getAlbumByAlbumTitle("Missing"));
    }

    @Test
    void saveAlbum_setsCreatedOnIfNull_andSaves() {

        Album toSave = Album.builder()
                .title("New")
                .genre(PrimaryGenre.POP)
                .year(2020)
                .description(null)
                .coverUrl("https://img")
                .price(new BigDecimal("19.99"))
                .artist(Artist.builder().artistName("A")
                        .primaryGenre(PrimaryGenre.POP)
                        .createdOn(LocalDateTime.now())
                        .build())
                .createdOn(null)
                .build();

        albumService.saveAlbum(toSave);

        assertNotNull(toSave.getCreatedOn());
        verify(albumRepo).save(toSave);
    }

    @Test
    void getAllAlbums_withGenre_usesGenreQuery() {

        when(albumRepo.findByGenreOrderByYearDesc(PrimaryGenre.CLASSICAL)).thenReturn(List.of(sampleAlbum));

        List<Album> result = albumService.getAllAlbums(PrimaryGenre.CLASSICAL);

        assertEquals(1, result.size());
        verify(albumRepo).findByGenreOrderByYearDesc(PrimaryGenre.CLASSICAL);
        verify(albumRepo, never()).findAllByOrderByYearDesc();
    }

    @Test
    void getAllAlbums_withoutGenre_usesDefaultQuery() {

        when(albumRepo.findAllByOrderByYearDesc()).thenReturn(List.of(sampleAlbum));

        List<Album> result = albumService.getAllAlbums(null);

        assertEquals(1, result.size());
        verify(albumRepo).findAllByOrderByYearDesc();
        verify(albumRepo, never()).findByGenreOrderByYearDesc(any());
    }

    @Test
    void getAllAlbums_plainDelegatesToFindAll() {

        when(albumRepo.findAll()).thenReturn(List.of(sampleAlbum));

        List<Album> result = albumService.getAllAlbums();

        assertEquals(1, result.size());
        verify(albumRepo).findAll();
    }

    @Test
    void getRandomAlbums_whenNoAlbums_returnsEmptyList() {

        when(albumRepo.findAll()).thenReturn(Collections.emptyList());

        List<Album> result = albumService.getRandomAlbums(5);

        assertTrue(result.isEmpty());
    }

    @Test
    void getRandomAlbums_limitsToRequestedSize() {

        List<Album> all = new ArrayList<>();
        IntStream.range(0, 5).forEach(i -> all.add(Album.builder()
                .title("A" + i)
                .genre(PrimaryGenre.ROCK)
                .year(2000 + i)
                .coverUrl("https://img" + i)
                .price(new BigDecimal("10.00"))
                .artist(Artist.builder().artistName("Ar")
                        .primaryGenre(PrimaryGenre.ROCK)
                        .createdOn(LocalDateTime.now())
                        .build())
                .createdOn(LocalDateTime.now())
                .build()));
        when(albumRepo.findAll()).thenReturn(all);

        List<Album> result = albumService.getRandomAlbums(3);

        assertEquals(3, result.size());
        assertTrue(all.containsAll(result));
    }

    @Test
    void getAlbumById_whenFound_returnsAlbum() {

        when(albumRepo.findById(albumId)).thenReturn(Optional.of(sampleAlbum));

        Album found = albumService.getAlbumById(albumId);

        assertNotNull(found);
        assertEquals("Test Album", found.getTitle());
    }

    @Test
    void getAlbumById_whenMissing_throwsDomainException() {

        when(albumRepo.findById(albumId)).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> albumService.getAlbumById(albumId));
    }

    @Test
    void saveAlbumFromDTO_whenTitleExists_throwsTitleAlreadyExistException() {

        SaveAlbumDTO dto = SaveAlbumDTO.builder()
                .title("Dup")
                .genre(PrimaryGenre.ROCK)
                .year(2001)
                .description("d")
                .coverUrl("https://img")
                .price(new BigDecimal("11.11"))
                .artistId(UUID.randomUUID())
                .build();
        when(albumRepo.findByTitle("Dup")).thenReturn(Optional.of(sampleAlbum));

        assertThrows(TitleAlreadyExistException.class, () -> albumService.saveAlbumFromDTO(dto));
        verify(albumRepo, never()).save(any());
    }

    @Test
    void saveAlbumFromDTO_whenValid_mapsAndSaves() {

        UUID artistId = UUID.randomUUID();
        Artist artist = Artist.builder()
                .artistName("Artist")
                .primaryGenre(PrimaryGenre.ROCK)
                .createdOn(LocalDateTime.now())
                .build();
        SaveAlbumDTO dto = SaveAlbumDTO.builder()
                .title("New")
                .genre(PrimaryGenre.ROCK)
                .year(2022)
                .description("desc")
                .coverUrl("https://img")
                .price(new BigDecimal("12.34"))
                .artistId(artistId)
                .build();

        when(artistService.getArtistById(artistId)).thenReturn(artist);
        when(albumRepo.findByTitle("New")).thenReturn(Optional.empty());

        albumService.saveAlbumFromDTO(dto);

        ArgumentCaptor<Album> captor = ArgumentCaptor.forClass(Album.class);
        verify(albumRepo).save(captor.capture());
        Album saved = captor.getValue();
        assertEquals("New", saved.getTitle());
        assertEquals(PrimaryGenre.ROCK, saved.getGenre());
        assertEquals(2022, saved.getYear());
        assertEquals(artist, saved.getArtist());
        assertNotNull(saved.getCreatedOn());
    }

    @Test
    void deleteAlbum_deletesRelatedItemsAndAlbum() {

        when(albumRepo.findById(albumId)).thenReturn(Optional.of(sampleAlbum));
        when(orderItemService.deleteAllItemsByAlbumId(albumId)).thenReturn(3);
        when(orderItemService.deleteEmptyOrders()).thenReturn(1);

        albumService.deleteAlbum(albumId);

        verify(orderItemService).deleteAllItemsByAlbumId(albumId);
        verify(orderItemService).deleteEmptyOrders();
        verify(albumRepo).delete(sampleAlbum);
    }

    @Test
    void getTotalAlbumCount_returnsRepoCount() {

        when(albumRepo.count()).thenReturn(42L);

        long result = albumService.getTotalAlbumCount();

        assertEquals(42L, result);
    }

    @Test
    void getAlbumsAddedThisMonth_usesFirstDayBoundaries() {

        ArgumentCaptor<LocalDateTime> startCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> endCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        when(albumRepo.countByCreatedOnBetween(any(), any())).thenReturn(5L);

        long result = albumService.getAlbumsAddedThisMonth();

        assertEquals(5L, result);
        verify(albumRepo).countByCreatedOnBetween(startCaptor.capture(), endCaptor.capture());

        LocalDate firstDay = LocalDate.now().withDayOfMonth(1);
        assertEquals(firstDay.atStartOfDay(), startCaptor.getValue());
        assertEquals(firstDay.plusMonths(1).atStartOfDay(), endCaptor.getValue());
    }

    @Test
    void getAverageAlbumPrice_whenNull_returnsZero() {

        when(albumRepo.findAveragePrice()).thenReturn(null);

        BigDecimal result = albumService.getAverageAlbumPrice();

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void getAverageAlbumPrice_whenPresent_returnsValue() {

        when(albumRepo.findAveragePrice()).thenReturn(12.5d);

        BigDecimal result = albumService.getAverageAlbumPrice();

        assertEquals(new BigDecimal("12.5"), result);
    }
}
