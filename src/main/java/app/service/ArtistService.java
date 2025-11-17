package app.service;

import app.model.dto.ArtistDTO;
import app.model.entity.Artist;

import java.util.List;
import java.util.UUID;

public interface ArtistService {

    Artist getArtistByArtistName(String artistName);

    void saveArtist(Artist artist);

    List<Artist> getAllArtists();

    List<ArtistDTO> getAllArtistsDTO();

    Artist getArtistById(UUID artistId);

    List<Artist> getLatestArtists(int limit);
}
