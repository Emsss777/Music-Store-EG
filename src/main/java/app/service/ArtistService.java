package app.service;

import app.model.entity.Artist;

public interface ArtistService {

    Artist getArtistByArtistName(String artistName);

    void saveArtist(Artist artistEntity);
}
