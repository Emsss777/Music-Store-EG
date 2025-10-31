package app.web.controller;

import app.mapper.OrderMapper;
import app.model.dto.OrderDTO;
import app.model.entity.Order;
import app.model.entity.User;
import app.security.AuthenticationMetadata;
import app.service.OrderService;
import app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @GetMapping(URL_MADE_ORDER)
    public ModelAndView getMadeOrderPage(@RequestParam String orderNumber) {

        Order order = orderService.getOrderByOrderNumber(orderNumber);
        OrderDTO orderDTO = OrderMapper.toDTO(order);

        ModelAndView modelAndView = new ModelAndView(VIEW_MADE_ORDER);
        modelAndView.addObject(MODEL_PAGE, VIEW_MADE_ORDER);
        modelAndView.addObject(MODEL_ORDER, orderDTO);
        modelAndView.addObject(MODEL_ORDER_ITEMS, orderDTO.getItems());

        return modelAndView;
    }

    @GetMapping(URL_MY_ORDERS)
    public ModelAndView getOrderPage(@AuthenticationPrincipal AuthenticationMetadata authMetadata) {

        User currentUser = userService.getUserById(authMetadata.getUserId());
        List<Order> orders = orderService.getOrdersByUser(currentUser);
        List<OrderDTO> orderDTOs = OrderMapper.toDTOList(orders);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_MY_ORDERS);
        modelAndView.addObject(MODEL_PAGE, VIEW_MY_ORDERS);
        modelAndView.addObject(MODEL_ORDERS, orderDTOs);

        return modelAndView;
    }

    @GetMapping(URL_ORDER_DETAILS + PARAM_ORDER_NUMBER)
    public ModelAndView getOrderDetailsPage(@PathVariable String orderNumber) {

        Order order = orderService.getOrderByOrderNumber(orderNumber);
        OrderDTO orderDTO = OrderMapper.toDTO(order);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_ORDER_DETAILS);
        modelAndView.addObject(MODEL_PAGE, VIEW_ORDER_DETAILS);
        modelAndView.addObject(MODEL_ORDER, orderDTO);

        return modelAndView;
    }
}
