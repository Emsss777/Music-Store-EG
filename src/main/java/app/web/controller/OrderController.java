package app.web.controller;

import app.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import static app.util.ModelAttributes.*;
import static app.util.UrlParams.PARAM_ORDER_NUMBER;
import static app.util.UrlPaths.*;
import static app.util.Views.*;

@Controller
@RequiredArgsConstructor
@RequestMapping(URL_USERS)
@SuppressWarnings("JvmTaintAnalysis")
public class OrderController {

    private final OrderService orderService;

    @GetMapping(URL_MADE_ORDER)
    public ModelAndView getMadeOrderPage(@RequestParam String orderNumber) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_MADE_ORDER);
        modelAndView.addObject(MODEL_PAGE, VIEW_MADE_ORDER);
        modelAndView.addObject(MODEL_ORDER, orderService.getOrderByOrderNumber(orderNumber));

        return modelAndView;
    }

    @GetMapping(URL_MY_ORDERS)
    public ModelAndView getOrderPage() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_MY_ORDERS);
        modelAndView.addObject(MODEL_PAGE, VIEW_MY_ORDERS);
        modelAndView.addObject(MODEL_ORDERS, orderService.getAllOrders());

        return modelAndView;
    }

    @GetMapping(URL_ORDER_DETAILS + PARAM_ORDER_NUMBER)
    public ModelAndView getOrderDetailsPage(@PathVariable String orderNumber) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_ORDER_DETAILS);
        modelAndView.addObject(MODEL_PAGE, VIEW_ORDER_DETAILS);
        modelAndView.addObject(MODEL_ORDER, orderService.getOrderByOrderNumber(orderNumber));

        return modelAndView;
    }
}
