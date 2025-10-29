package app.service;

import app.model.dto.CartItemDTO;
import app.model.dto.CartSummaryDTO;
import app.model.entity.Album;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface CartService {

    void addToCart(HttpSession session, Album album);

    void removeFromCart(HttpSession session, UUID albumId);

    List<CartItemDTO> getCartItems(HttpSession session);

    void clearCart(HttpSession session);

    int getCartItemCount(HttpSession session);

    BigDecimal getCartTotal(HttpSession session);

    List<CartItemDTO> getOrInitializeCart(HttpSession session);

    CartSummaryDTO getCartSummary(HttpSession session);
}
