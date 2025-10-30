package app.service;

import app.model.enums.PrimaryGenre;

import java.util.List;

public interface GenreService {

    List<PrimaryGenre> getRandomGenres(int limit);
}
