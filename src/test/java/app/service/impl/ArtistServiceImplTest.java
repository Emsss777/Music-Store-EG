package app.service.impl;

import app.exception.DomainException;
import app.model.entity.Artist;
import app.model.enums.PrimaryGenre;
import app.repository.ArtistRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtistServiceImplTest {

    @Mock
    private ArtistRepo artistRepo;

    @InjectMocks
    private ArtistServiceImpl artistService;

    private Artist sampleArtist;
    private UUID artistId;

    @BeforeEach
    void setUp() {

        artistId = UUID.randomUUID();
        sampleArtist = Artist.builder()
                .artistName("Sample Artist")
                .primaryGenre(PrimaryGenre.ROCK)
                .createdOn(LocalDateTime.now())
                .build();
    }

    @Test
    void getArtistByArtistName_whenFound_returnsArtist() {

        when(artistRepo.findByArtistName("Name")).thenReturn(Optional.of(sampleArtist));

        Artist found = artistService.getArtistByArtistName("Name");

        assertNotNull(found);
        assertEquals("Sample Artist", found.getArtistName());
        verify(artistRepo).findByArtistName("Name");
    }

    @Test
    void getArtistByArtistName_whenMissing_throwsDomainException() {

        when(artistRepo.findByArtistName("Missing")).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> artistService.getArtistByArtistName("Missing"));
    }

    @Test
    void saveArtist_setsCreatedOnIfNull_andSaves() {

        Artist toSave = Artist.builder()
                .artistName("New")
                .primaryGenre(PrimaryGenre.POP)
                .createdOn(null)
                .build();

        artistService.saveArtist(toSave);

        assertNotNull(toSave.getCreatedOn());
        verify(artistRepo).save(toSave);
    }

    @Test
    void getAllArtists_delegatesToRepo() {

        when(artistRepo.findAll()).thenReturn(List.of(sampleArtist));

        List<Artist> result = artistService.getAllArtists();

        assertEquals(1, result.size());
        verify(artistRepo).findAll();
    }

    @Test
    void getArtistById_whenFound_returnsArtist() {

        when(artistRepo.findById(artistId)).thenReturn(Optional.of(sampleArtist));

        Artist result = artistService.getArtistById(artistId);

        assertEquals("Sample Artist", result.getArtistName());
    }

    @Test
    void getArtistById_whenMissing_throwsDomainException() {

        when(artistRepo.findById(artistId)).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> artistService.getArtistById(artistId));
    }

    @Test
    void getLatestArtists_buildsCorrectPageRequest_andReturnsContent() {

        List<Artist> latest = List.of(sampleArtist);
        ArgumentCaptor<PageRequest> pageRequestCaptor = ArgumentCaptor.forClass(PageRequest.class);
        when(artistRepo.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(latest));

        List<Artist> result = artistService.getLatestArtists(3);

        assertEquals(latest, result);
        verify(artistRepo).findAll(pageRequestCaptor.capture());
        PageRequest pr = pageRequestCaptor.getValue();
        assertEquals(0, pr.getPageNumber());
        assertEquals(3, pr.getPageSize());
        assertEquals(Sort.by("createdOn").descending(), pr.getSort());
    }
}
