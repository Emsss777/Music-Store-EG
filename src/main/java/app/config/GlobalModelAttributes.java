package app.config;

import app.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import static app.util.ModelAttributes.MODEL_ITEM_COUNT;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributes {

    private final CartService cartService;

    @ModelAttribute(MODEL_ITEM_COUNT)
    public int getCartItemCount(HttpSession session) {
        if (session == null) {
            return 0;
        }
        return cartService.getCartItemCount(session);
    }
}
