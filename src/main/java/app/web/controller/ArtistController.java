package app.web.controller;

import app.mapper.ArtistMapper;
import app.model.dto.ArtistDTO;
import app.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static app.util.ModelAttributes.MODEL_LATEST_ARTISTS;
import static app.util.UrlPaths.*;
import static app.util.Views.VIEW_ADMIN_ADD_ARTIST;

@Controller
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    @GetMapping(URL_ADMIN_ARTISTS + URL_ADD)
    public ModelAndView getAddArtistPage() {

        List<ArtistDTO> latestArtists = ArtistMapper.toDTOList(artistService.getLatestArtists(4));

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_ADMIN_ADD_ARTIST);
        modelAndView.addObject(MODEL_LATEST_ARTISTS, latestArtists);

        return modelAndView;
    }
}
