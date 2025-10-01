package app.controller;

import app.model.dto.RegisterDTO;
import app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import static app.util.ModelAttributes.MODEL_REGISTER_DTO;
import static app.util.Redirects.REDIRECT_LOGIN;
import static app.util.UrlPaths.*;
import static app.util.Views.*;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {

        this.userService = userService;
    }

    @GetMapping(URL_LOGIN)
    public ModelAndView getLoginPage() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_LOGIN);
        //modelAndView.addObject(MODEL_LOGIN_DTO, new LoginDTO());

        return modelAndView;
    }

    @GetMapping(URL_REGISTER)
    public ModelAndView getRegisterPage() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_REGISTER);
        modelAndView.addObject(MODEL_REGISTER_DTO, new RegisterDTO());

        return modelAndView;
    }

    @PostMapping(URL_REGISTER)
    public ModelAndView doRegister(@Valid RegisterDTO registerDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView(VIEW_REGISTER);
        }

        userService.registerUser(registerDTO);

        return new ModelAndView(REDIRECT_LOGIN);
    }
}
