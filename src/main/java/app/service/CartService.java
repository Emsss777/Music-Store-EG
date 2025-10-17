package app.service;

import app.model.dto.CartItemDTO;
import app.model.entity.AlbumEntity;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface CartService {

    void addToCart(HttpSession session, AlbumEntity album);

    void removeFromCart(HttpSession session, UUID albumId);

    List<CartItemDTO> getCartItems(HttpSession session);

    void clearCart(HttpSession session);

    int getCartItemCount(HttpSession session);

    BigDecimal getCartTotal(HttpSession session);
}
