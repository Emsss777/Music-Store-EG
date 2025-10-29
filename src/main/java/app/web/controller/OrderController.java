package app.web.controller;

import app.model.dto.OrderItemDTO;
import app.model.entity.Order;
import app.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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

        Order order = orderService.getOrderByOrderNumber(orderNumber);
        List<OrderItemDTO> orderItems = orderService.getOrderItems(order.getId());

        ModelAndView modelAndView = new ModelAndView(VIEW_MADE_ORDER);
        modelAndView.addObject(MODEL_PAGE, VIEW_MADE_ORDER);
        modelAndView.addObject(MODEL_ORDER, order);
        modelAndView.addObject(MODEL_ORDER_ITEMS, orderItems);

        return modelAndView;
    }

    @GetMapping(URL_MY_ORDERS)
    public ModelAndView getOrderPage() {

        List<Order> orders = orderService.getAllOrders();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_MY_ORDERS);
        modelAndView.addObject(MODEL_PAGE, VIEW_MY_ORDERS);
        modelAndView.addObject(MODEL_ORDERS, orders);

        return modelAndView;
    }

    @GetMapping(URL_ORDER_DETAILS + PARAM_ORDER_NUMBER)
    public ModelAndView getOrderDetailsPage(@PathVariable String orderNumber) {

        Order order = orderService.getOrderByOrderNumber(orderNumber);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_ORDER_DETAILS);
        modelAndView.addObject(MODEL_PAGE, VIEW_ORDER_DETAILS);
        modelAndView.addObject(MODEL_ORDER, order);

        return modelAndView;
    }
}
