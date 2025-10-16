package app.controller;

import app.exception.PasswordMismatchException;
import app.exception.UsernameAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static app.util.ModelAttributes.*;
import static app.util.Redirects.REDIRECT_REGISTER;
import static app.util.Views.VIEW_INTERNAL_SERVER_ERROR;
import static app.util.Views.VIEW_PAGE_NOT_FOUND;

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

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            AccessDeniedException.class,
            NoResourceFoundException.class,
            MethodArgumentTypeMismatchException.class,
            MissingRequestValueException.class
    })
    public ModelAndView handleNotFoundExceptions() {

        return new ModelAndView(VIEW_PAGE_NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleAnyException(Exception exception) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_INTERNAL_SERVER_ERROR);
        modelAndView.addObject(MODEL_ERROR_MESSAGE, exception.getClass().getSimpleName());

        return modelAndView;
    }
}
