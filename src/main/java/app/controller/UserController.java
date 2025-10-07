package app.controller;

import app.mapper.UserEditMapper;
import app.mapper.UserProfileMapper;
import app.model.dto.UserEditDTO;
import app.model.dto.UserProfileDTO;
import app.model.entity.UserEntity;
import app.security.AuthenticationMetadata;
import app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

import static app.util.ModelAttributes.*;
import static app.util.Redirects.REDIRECT_PROFILE;
import static app.util.UrlParams.PARAM_ID;
import static app.util.UrlPaths.*;
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
        modelAndView.addObject(MODEL_PAGE, VIEW_PROFILE);
        modelAndView.addObject(MODEL_USER, currentUser);

        return modelAndView;
    }

    @GetMapping(PARAM_ID + URL_PROFILE)
    public ModelAndView showEditProfile(@PathVariable UUID id) {

        UserEntity user = userService.getUserById(id);
        UserProfileDTO userProfileDTO = UserProfileMapper.toSafeDTO(user);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_EDIT_PROFILE);
        modelAndView.addObject(MODEL_USER, userProfileDTO);
        modelAndView.addObject(MODEL_USER_EDIT_DTO, UserEditMapper.mapUserToUserEditDTO(user));

        return modelAndView;
    }

    @PutMapping(PARAM_ID + URL_PROFILE)
    public ModelAndView updateUserProfile(@PathVariable UUID id,
                                          @Valid UserEditDTO userEditDTO, BindingResult bindingResult) {

        UserEntity user = userService.getUserById(id);
        UserProfileDTO userProfileDTO = UserProfileMapper.toSafeDTO(user);

        if (bindingResult.hasErrors()) {

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName(VIEW_EDIT_PROFILE);
            modelAndView.addObject(MODEL_USER, userProfileDTO);
            modelAndView.addObject(MODEL_USER_EDIT_DTO, userEditDTO);

            return modelAndView;
        }

        userService.editUserDetails(id, userEditDTO);

        return new ModelAndView(REDIRECT_PROFILE);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(URL_ADMIN_DASHBOARD)
    public ModelAndView getAdminPage(@AuthenticationPrincipal AuthenticationMetadata authMetadata) {

        UserEntity currentUser = userService.getUserById(authMetadata.getUserId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_ADMIN_DASHBOARD);
        modelAndView.addObject(MODEL_PAGE, VIEW_ADMIN_DASHBOARD);
        modelAndView.addObject(MODEL_USER, currentUser);

        return modelAndView;
    }
}
