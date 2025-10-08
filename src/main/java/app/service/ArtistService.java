package app.service;

import app.model.entity.ArtistEntity;

public interface ArtistService {

    ArtistEntity getArtistByArtistName(String artistName);

    void saveArtist(ArtistEntity artistEntity);
}
