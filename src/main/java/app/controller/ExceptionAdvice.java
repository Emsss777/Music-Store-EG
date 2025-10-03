package app.controller;

import app.exception.PasswordMismatchException;
import app.exception.UsernameAlreadyExistException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static app.util.ModelAttributes.*;
import static app.util.Redirects.REDIRECT_REGISTER;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(UsernameAlreadyExistException.class)
    public String handleUsernameAlreadyExists(RedirectAttributes redirectAttributes,
                                              UsernameAlreadyExistException exception) {

        String message = exception.getMessage();

        redirectAttributes.addFlashAttribute(MODEL_USERNAME_ALREADY_EXISTS, message);

        return REDIRECT_REGISTER;
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public String handlePasswordMismatchException(RedirectAttributes redirectAttributes,
                                                  PasswordMismatchException exception) {

        String message = exception.getMessage();

        redirectAttributes.addFlashAttribute(MODEL_PASSWORD_MISMATCH_EXCEPTION, message);

        return REDIRECT_REGISTER;
    }
}
