package app.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import static app.util.UrlPaths.*;
import static app.util.Views.VIEW_ADMIN_ADD_ARTIST;

@Controller
public class ArtistController {

    @GetMapping(URL_ADMIN_ARTISTS + URL_ADD)
    public ModelAndView getAddArtistPage() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_ADMIN_ADD_ARTIST);

        return modelAndView;
    }
}
