package app.controller;

import app.exception.UsernameAlreadyExistException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static app.util.FlashAttributes.USERNAME_ALREADY_EXISTS;
import static app.util.Redirects.REDIRECT_REGISTER;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(UsernameAlreadyExistException.class)
    public String handleUsernameAlreadyExist(RedirectAttributes redirectAttributes,
                                             UsernameAlreadyExistException exception) {

        String message = exception.getMessage();

        redirectAttributes.addFlashAttribute(USERNAME_ALREADY_EXISTS, message);

        return REDIRECT_REGISTER;
    }
}
