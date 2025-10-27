package app.web.controller;

import app.model.entity.User;
import app.security.AuthenticationMetadata;
import app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

import static app.util.ModelAttributes.*;
import static app.util.Redirects.REDIRECT_USERS;
import static app.util.UrlParams.PARAM_ID;
import static app.util.UrlPaths.*;
import static app.util.Views.VIEW_ADMIN_DASHBOARD;
import static app.util.Views.VIEW_USERS;

@Controller
@RequiredArgsConstructor
@RequestMapping(URL_ADMIN)
public class AdminController {

    private final UserService userService;

    @GetMapping(URL_USERS)
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView getAllUsers() {

        List<User> users = userService.getAllUsers();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_USERS);
        modelAndView.addObject(MODEL_USERS, users);

        return modelAndView;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(URL_ADMIN_DASHBOARD)
    public ModelAndView getAdminPage(@AuthenticationPrincipal AuthenticationMetadata authMetadata) {

        User currentUser = userService.getUserById(authMetadata.getUserId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_ADMIN_DASHBOARD);
        modelAndView.addObject(MODEL_PAGE, VIEW_ADMIN_DASHBOARD);
        modelAndView.addObject(MODEL_USER, currentUser);

        return modelAndView;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(URL_USERS + PARAM_ID + URL_STATUS)
    public String changeUserStatus(@PathVariable UUID id) {

        userService.changeStatus(id);

        return REDIRECT_USERS;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(URL_USERS + PARAM_ID + URL_ROLE)
    public String changeUserRole(@PathVariable UUID id) {

        userService.changeRole(id);

        return REDIRECT_USERS;
    }
}
