package app.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import static app.util.ModelAttributes.*;
import static app.util.UrlPaths.URL_HOME;
import static app.util.UrlPaths.URL_ROOT;
import static app.util.Views.VIEW_HOME;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping({URL_ROOT, URL_HOME})
    public ModelAndView getHomePage() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_HOME);
        modelAndView.addObject(MODEL_PAGE, VIEW_HOME);

        return modelAndView;
    }
}
