package app.web.controller;

import app.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static app.util.ModelAttributes.*;
import static app.util.UrlPaths.*;
import static app.util.Views.VIEW_MY_ORDERS;

@Controller
@RequiredArgsConstructor
@RequestMapping(URL_USERS)
public class OrderController {

    private final OrderService orderService;

    @GetMapping(URL_MY_ORDERS)
    public ModelAndView getOrderPage() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_MY_ORDERS);
        modelAndView.addObject(MODEL_PAGE, VIEW_MY_ORDERS);
        modelAndView.addObject(MODEL_ORDERS, orderService.getAllOrders());

        return modelAndView;
    }
}
