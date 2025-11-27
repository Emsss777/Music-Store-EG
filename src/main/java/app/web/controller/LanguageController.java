package app.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.Locale;

import static app.util.Redirects.*;
import static app.util.UrlPaths.URL_CHANGE_LANGUAGE;

@Controller
@RequiredArgsConstructor
public class LanguageController {

    @GetMapping(URL_CHANGE_LANGUAGE)
    public String changeLanguage(@RequestParam String lang, HttpServletRequest request, HttpServletResponse response) {

        Locale locale = new Locale(lang);
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);

        if (localeResolver != null) {
            localeResolver.setLocale(request, response, locale);
        }

        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            return REDIRECT + referer;
        }
        return REDIRECT_HOME;
    }
}
