package app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static app.util.UrlPaths.URL_ROOT;
import static app.util.Views.VIEW_HOME;

@Controller
public class InitController {

    @GetMapping(URL_ROOT)
    public String getIndexPage() {

        return VIEW_HOME;
    }
}
