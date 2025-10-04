package app.controller;

import app.mapper.UserProfileMapper;
import app.model.dto.UserProfileDTO;
import app.model.entity.UserEntity;
import app.security.AuthenticationMetadata;
import app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

import static app.util.ModelAttributes.*;
import static app.util.UrlParams.PARAM_ID;
import static app.util.UrlPaths.*;
import static app.util.ValueAttributes.ATTRIBUTE_PROFILE;
import static app.util.Views.*;

@Controller
@RequestMapping(URL_USERS)
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
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

    @GetMapping(PARAM_ID + URL_PROFILE)
    public ModelAndView showEditProfile(@PathVariable UUID id) {

        UserEntity user = userService.getUserById(id);
        UserProfileDTO safe = UserProfileMapper.toSafeDTO(user);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_EDIT_PROFILE);
        modelAndView.addObject(MODEL_USER, safe);

        return modelAndView;
    }
}
