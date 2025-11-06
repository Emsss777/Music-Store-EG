package app.validation;

import app.model.dto.OrderAccessDTO;
import app.model.entity.Order;
import app.model.entity.User;
import app.service.OrderService;
import app.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderOwnerValidator implements ConstraintValidator<OrderOwner, OrderAccessDTO> {

    private final UserService userService;
    private final OrderService orderService;

    @Override
    public boolean isValid(OrderAccessDTO orderAccessDTO, ConstraintValidatorContext context) {

        if (orderAccessDTO == null || orderAccessDTO.getOrderId() == null) {
            return true;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        UUID userId = ((app.security.AuthenticationMetadata) auth.getPrincipal()).getUserId();

        User user = userService.getUserById(userId);
        Order order = orderService.getOrderById(orderAccessDTO.getOrderId());

        return order.getOwner().getId().equals(user.getId());
    }
}
