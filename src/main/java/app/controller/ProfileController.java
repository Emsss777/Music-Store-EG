package app.controller;

import app.model.entity.UserEntity;
import app.security.AuthenticationMetadata;
import app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import static app.util.ModelAttributes.*;
import static app.util.UrlPaths.URL_PROFILE;
import static app.util.ValueAttributes.ATTRIBUTE_PROFILE;
import static app.util.Views.VIEW_PROFILE;

@Controller
public class ProfileController {

    private final UserService userService;

    @Autowired
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(URL_PROFILE)
    public ModelAndView getProfilePage(@AuthenticationPrincipal AuthenticationMetadata authMetadata) {

        UserEntity currentUser = userService.getUserById(authMetadata.getUserId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_PROFILE);
        modelAndView.addObject(MODEL_PAGE, ATTRIBUTE_PROFILE);
        modelAndView.addObject(MODEL_USER, currentUser);

        return modelAndView;
    }
}
