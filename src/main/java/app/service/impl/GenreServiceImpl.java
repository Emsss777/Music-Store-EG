package app.service.impl;

import app.model.enums.PrimaryGenre;
import app.service.GenreService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {

    @Override
    public List<PrimaryGenre> getRandomGenres(int limit) {

        List<PrimaryGenre> allGenres = new ArrayList<>(List.of(PrimaryGenre.values()));

        if (allGenres.isEmpty()) {
            return Collections.emptyList();
        }

        Collections.shuffle(allGenres);

        return allGenres.stream()
                .limit(limit)
                .toList();
    }
}
