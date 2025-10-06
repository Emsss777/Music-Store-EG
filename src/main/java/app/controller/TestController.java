package app.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static app.util.ExceptionMessages.INTERNAL_SERVER_ERROR_TEST;
import static app.util.UrlPaths.URL_DEV_TEST_500;

@Profile("dev")
@Controller
public class TestController {

    @GetMapping(URL_DEV_TEST_500)
    public String triggerError() {

        throw new IllegalStateException(INTERNAL_SERVER_ERROR_TEST);
    }
}
