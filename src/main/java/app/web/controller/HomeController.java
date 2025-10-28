package app.web.controller;

import app.model.entity.Album;
import app.model.enums.PrimaryGenre;
import app.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static app.util.ModelAttributes.*;
import static app.util.UrlPaths.URL_HOME;
import static app.util.UrlPaths.URL_ROOT;
import static app.util.Views.VIEW_HOME;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final AlbumService albumService;

    @GetMapping({URL_ROOT, URL_HOME})
    public ModelAndView getHomePage() {

        List<Album> allAlbums = albumService.getAllAlbums();
        Collections.shuffle(allAlbums);
        List<Album> randomAlbums = allAlbums.stream()
                .limit(4)
                .toList();

        List<PrimaryGenre> allGenres = new ArrayList<>(List.of(PrimaryGenre.values()));
        Collections.shuffle(allGenres);
        List<PrimaryGenre> randomGenres = allGenres.stream()
                .limit(5)
                .toList();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_HOME);
        modelAndView.addObject(MODEL_PAGE, VIEW_HOME);
        modelAndView.addObject(MODEL_ALBUMS, randomAlbums);
        modelAndView.addObject(MODEL_GENRES, randomGenres);

        return modelAndView;
    }
}
