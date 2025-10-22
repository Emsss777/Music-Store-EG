package app.service.impl;

import app.exception.DomainException;
import app.model.entity.Artist;
import app.repository.ArtistRepo;
import app.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static app.util.ExceptionMessages.ARTIST_DOES_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepo artistRepo;

    @Override
    public Artist getArtistByArtistName(String artistName) {

        return artistRepo.findByArtistName(artistName).orElseThrow(() ->
                new DomainException(ARTIST_DOES_NOT_EXIST.formatted(artistName)));
    }

    @Override
    public void saveArtist(Artist artistEntity) {

        artistRepo.save(artistEntity);
    }
}
