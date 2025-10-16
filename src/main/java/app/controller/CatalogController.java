package app.controller;

import app.model.enums.PrimaryGenre;
import app.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import static app.util.ModelAttributes.*;
import static app.util.UrlPaths.URL_CATALOG;
import static app.util.Views.VIEW_CATALOG;

@Controller
@RequiredArgsConstructor
public class CatalogController {

    private final AlbumService albumService;

    @GetMapping(URL_CATALOG)
    public ModelAndView getCatalogPage(@RequestParam(required = false) PrimaryGenre genre) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_CATALOG);
        modelAndView.addObject(MODEL_PAGE, VIEW_CATALOG);

        if (genre != null) {
            modelAndView.addObject(MODEL_ALBUMS, albumService.getAlbumsByGenre(genre));
        } else {
            modelAndView.addObject(MODEL_ALBUMS, albumService.getAllAlbums());
        }

        modelAndView.addObject(MODEL_SELECTED_GENRE, genre);
        modelAndView.addObject(MODEL_GENRES, PrimaryGenre.values());

        return modelAndView;
    }
}
