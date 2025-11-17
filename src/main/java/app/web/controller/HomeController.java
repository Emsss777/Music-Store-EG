package app.web.controller;

import app.model.dto.AlbumDTO;
import app.model.enums.PrimaryGenre;
import app.service.AlbumService;
import app.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static app.util.ModelAttributes.*;
import static app.util.UrlPaths.URL_HOME;
import static app.util.UrlPaths.URL_ROOT;
import static app.util.Views.VIEW_HOME;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final AlbumService albumService;
    private final GenreService genreService;

    @GetMapping({URL_ROOT, URL_HOME})
    public ModelAndView getHomePage() {

        List<AlbumDTO> albumDTOs = albumService.getRandomAlbumsDTO(4);

        List<PrimaryGenre> randomGenres = genreService.getRandomGenres(5);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_HOME);
        modelAndView.addObject(MODEL_PAGE, VIEW_HOME);
        modelAndView.addObject(MODEL_ALBUMS, albumDTOs);
        modelAndView.addObject(MODEL_GENRES, randomGenres);

        return modelAndView;
    }
}
