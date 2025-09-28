package app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import static app.util.UrlPaths.*;
import static app.util.Views.*;

@Controller
public class IndexController {

    @GetMapping(URL_ROOT)
    public String getIndexPage() {

        return VIEW_HOME;
    }

    @GetMapping(URL_LOGIN)
    public ModelAndView getLoginPage() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_LOGIN);
        //modelAndView.addObject(MODEL_LOGIN_DTO, new LoginDTO());

        return modelAndView;
    }
}
