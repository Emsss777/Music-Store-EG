package app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import static app.util.ModelAttributes.*;
import static app.util.UrlPaths.URL_CART;
import static app.util.Views.VIEW_CART;

@Controller
@RequiredArgsConstructor
public class CartController {

    @GetMapping(URL_CART)
    public ModelAndView getCartPage() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_CART);
        modelAndView.addObject(MODEL_PAGE, VIEW_CART);

        return modelAndView;
    }
}
