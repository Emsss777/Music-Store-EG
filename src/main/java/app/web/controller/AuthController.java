package app.web.controller;

import app.model.dto.AlbumDTO;
import app.model.dto.LoginDTO;
import app.model.dto.RegisterDTO;
import app.service.AlbumService;
import app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static app.util.ExceptionMessages.INCORRECT_USERNAME_OR_PASSWORD;
import static app.util.ModelAttributes.*;
import static app.util.Redirects.REDIRECT_LOGIN;
import static app.util.UrlPaths.*;
import static app.util.Views.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AlbumService albumService;

    @GetMapping(URL_LOGIN)
    public ModelAndView getLoginPage(@RequestParam(value = "error", required = false) String errorParam) {

        List<AlbumDTO> albumDTOs = albumService.getRandomAlbumsDTO(6);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_LOGIN);
        modelAndView.addObject(MODEL_LOGIN_DTO, new LoginDTO());
        modelAndView.addObject(MODEL_ALBUMS, albumDTOs);

        if (errorParam != null) {
            modelAndView.addObject(MODEL_ERROR_MESSAGE, INCORRECT_USERNAME_OR_PASSWORD);
        }

        return modelAndView;
    }

    @GetMapping(URL_REGISTER)
    public ModelAndView getRegisterPage() {

        List<AlbumDTO> albumDTOs = albumService.getRandomAlbumsDTO(6);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_REGISTER);
        modelAndView.addObject(MODEL_REGISTER_DTO, new RegisterDTO());
        modelAndView.addObject(MODEL_ALBUMS, albumDTOs);

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
