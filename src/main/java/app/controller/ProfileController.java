package app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import static app.util.UrlPaths.URL_PROFILE;
import static app.util.Views.VIEW_PROFILE;

@Controller
public class ProfileController {

    @GetMapping(URL_PROFILE)
    public ModelAndView getProfilePage() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_PROFILE);

        return modelAndView;
    }
}
