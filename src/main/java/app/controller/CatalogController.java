package app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import static app.util.UrlPaths.URL_CATALOG;
import static app.util.Views.VIEW_CATALOG;

@Controller
public class CatalogController {

    @GetMapping(URL_CATALOG)
    public ModelAndView getCatalogPage() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_CATALOG);

        return modelAndView;
    }
}
