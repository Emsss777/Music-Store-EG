package app.service;

import app.model.entity.Artist;

import java.util.List;
import java.util.UUID;

public interface ArtistService {

    Artist getArtistByArtistName(String artistName);

    void saveArtist(Artist artist);

    List<Artist> getAllArtists();

    Artist getArtistById(UUID artistId);
}
