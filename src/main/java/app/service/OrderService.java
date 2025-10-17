package app.service;

import app.model.dto.CartItemDTO;
import app.model.dto.CheckoutDTO;
import app.model.entity.OrderEntity;
import app.model.entity.UserEntity;

import java.util.List;

public interface OrderService {

    OrderEntity createOrder(CheckoutDTO checkoutDTO, List<CartItemDTO> cartItems, UserEntity user);
}
