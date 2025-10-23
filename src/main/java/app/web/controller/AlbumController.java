package app.web.controller;

import app.model.dto.SaveAlbumDTO;
import app.model.entity.Artist;
import app.service.AlbumService;
import app.service.ArtistService;
import app.web.util.PageBuilder;
import app.model.enums.PrimaryGenre;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static app.util.ModelAttributes.*;
import static app.util.Redirects.REDIRECT_ADMIN_ALBUMS;
import static app.util.UrlPaths.*;
import static app.util.Views.*;

@Controller
@RequiredArgsConstructor
@SuppressWarnings("JvmTaintAnalysis")
public class AlbumController {

    private final PageBuilder pageBuilder;
    private final AlbumService albumService;
    private final ArtistService artistService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(URL_ADMIN_ALBUMS)
    public ModelAndView getAdminAlbumsPage(@RequestParam(required = false) PrimaryGenre genre) {

        return pageBuilder.buildAlbumsPage(VIEW_ADMIN_ALBUMS, genre);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(URL_ADMIN_ALBUMS + URL_ADD)
    public ModelAndView getAddAlbumPage() {

        List<Artist> artists = artistService.getAllArtists();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_ADMIN_ADD_ALBUM);
        modelAndView.addObject(MODEL_ARTISTS, artists);
        modelAndView.addObject(MODEL_GENRES, PrimaryGenre.values());
        modelAndView.addObject(MODEL_SAVE_ALBUM_DTO, new SaveAlbumDTO());

        return modelAndView;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(URL_ADMIN_ALBUMS + URL_ADD)
    public ModelAndView addAlbum(@Valid @ModelAttribute SaveAlbumDTO saveAlbumDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<Artist> artists = artistService.getAllArtists();

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName(VIEW_ADMIN_ADD_ALBUM);
            modelAndView.addObject(MODEL_ARTISTS, artists);
            modelAndView.addObject(MODEL_GENRES, PrimaryGenre.values());
            modelAndView.addObject(MODEL_SAVE_ALBUM_DTO, saveAlbumDTO);

            return modelAndView;
        }

        albumService.saveAlbumFromDTO(saveAlbumDTO);

        return new ModelAndView(REDIRECT_ADMIN_ALBUMS);
    }
}
