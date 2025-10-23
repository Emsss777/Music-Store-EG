package app.service.impl;

import app.exception.DomainException;
import app.model.entity.Artist;
import app.repository.ArtistRepo;
import app.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
    public void saveArtist(Artist artist) {

        artistRepo.save(artist);
    }

    @Override
    public List<Artist> getAllArtists() {

        return artistRepo.findAll();
    }

    @Override
    public Artist getArtistById(UUID artistId) {

        return artistRepo.findById(artistId).orElseThrow(() ->
                new DomainException(ARTIST_DOES_NOT_EXIST.formatted(artistId)));
    }
}
