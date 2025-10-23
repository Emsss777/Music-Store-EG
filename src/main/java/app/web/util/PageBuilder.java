package app.web.util;

import app.model.enums.PrimaryGenre;
import app.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import static app.util.ModelAttributes.*;
import static app.util.ModelAttributes.MODEL_GENRES;

@Component
@RequiredArgsConstructor
public class PageBuilder {

    private final AlbumService albumService;

    public ModelAndView buildAlbumsPage(String viewName, PrimaryGenre genre) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(viewName);
        modelAndView.addObject(MODEL_PAGE, viewName);
        modelAndView.addObject(MODEL_ALBUMS, albumService.getAllAlbums(genre));
        modelAndView.addObject(MODEL_SELECTED_GENRE, genre);
        modelAndView.addObject(MODEL_GENRES, PrimaryGenre.values());

        return modelAndView;
    }
}
