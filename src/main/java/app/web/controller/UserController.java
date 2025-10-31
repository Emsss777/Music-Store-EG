package app.web.controller;

import app.mapper.UserEditMapper;
import app.mapper.UserProfileMapper;
import app.model.dto.UserEditDTO;
import app.model.dto.UserProfileDTO;
import app.model.entity.User;
import app.security.AuthenticationMetadata;
import app.service.OrderService;
import app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

import static app.util.ModelAttributes.*;
import static app.util.Redirects.*;
import static app.util.UrlParams.PARAM_ID;
import static app.util.UrlPaths.*;
import static app.util.Views.*;

@Controller
@RequiredArgsConstructor
@RequestMapping(URL_USERS)
public class UserController {

    private final UserService userService;
    private final OrderService orderService;

    @GetMapping(URL_PROFILE)
    public ModelAndView getProfilePage(@AuthenticationPrincipal AuthenticationMetadata authMetadata) {

        User currentUser = userService.getUserById(authMetadata.getUserId());
        int totalAlbumsPurchased = orderService.getTotalAlbumsPurchasedByUser(currentUser);
        java.math.BigDecimal totalAmountSpent = orderService.getTotalAmountSpentByUser(currentUser);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_PROFILE);
        modelAndView.addObject(MODEL_PAGE, VIEW_PROFILE);
        modelAndView.addObject(MODEL_USER, UserProfileMapper.toSafeDTO(currentUser));
        modelAndView.addObject(MODEL_TOTAL_ALBUM_PURCHASED, totalAlbumsPurchased);
        modelAndView.addObject(MODEL_TOTAL_AMOUNT_SPENT, totalAmountSpent);

        return modelAndView;
    }

    @GetMapping(PARAM_ID + URL_PROFILE)
    public ModelAndView showEditProfile(@PathVariable UUID id) {

        User user = userService.getUserById(id);
        UserProfileDTO userProfileDTO = UserProfileMapper.toSafeDTO(user);
        UserEditDTO userEditDTO = UserEditMapper.mapUserToUserEditDTO(user);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_EDIT_PROFILE);
        modelAndView.addObject(MODEL_USER, userProfileDTO);
        modelAndView.addObject(MODEL_USER_EDIT_DTO, userEditDTO);

        return modelAndView;
    }

    @PutMapping(PARAM_ID + URL_PROFILE)
    public ModelAndView updateUserProfile(@PathVariable UUID id,
                                          @Valid UserEditDTO userEditDTO, BindingResult bindingResult) {

        User user = userService.getUserById(id);
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
}
