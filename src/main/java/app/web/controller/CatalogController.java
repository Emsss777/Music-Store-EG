package app.web.controller;

import app.web.util.PageBuilder;
import app.model.entity.Album;
import app.model.enums.PrimaryGenre;
import app.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

import static app.util.ModelAttributes.*;
import static app.util.UrlParams.PARAM_ID;
import static app.util.UrlPaths.URL_ALBUM;
import static app.util.UrlPaths.URL_CATALOG;
import static app.util.Views.VIEW_ALBUM;
import static app.util.Views.VIEW_CATALOG;

@Controller
@RequiredArgsConstructor
@SuppressWarnings("JvmTaintAnalysis")
public class CatalogController {

    private final AlbumService albumService;
    private final PageBuilder pageBuilder;

    @GetMapping(URL_CATALOG)
    public ModelAndView getCatalogPage(@RequestParam(required = false) PrimaryGenre genre) {

        return pageBuilder.buildAlbumsPage(VIEW_CATALOG, genre);
    }

    @GetMapping(URL_ALBUM + PARAM_ID)
    public ModelAndView getAlbumDetailsPage(@PathVariable UUID id) {

        Album album = albumService.getAlbumById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_ALBUM);
        modelAndView.addObject(MODEL_PAGE, VIEW_ALBUM);
        modelAndView.addObject(MODEL_ALBUM, album);

        return modelAndView;
    }
}
