package app.service.impl;

import app.model.dto.CartItemDTO;
import app.model.entity.Album;
import app.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static app.util.ModelAttributes.MODEL_CART;

@Service
public class CartServiceImpl implements CartService {

    @Override
    public void addToCart(HttpSession session, Album album) {

        List<CartItemDTO> cartItems = getCartItems(session);

        CartItemDTO existingItem = cartItems.stream()
                .filter(item -> item.getAlbumId().equals(album.getId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
        } else {
            CartItemDTO newItem = CartItemDTO.builder()
                    .albumId(album.getId())
                    .title(album.getTitle())
                    .artistName(album.getArtist().getArtistName())
                    .coverUrl(album.getCoverUrl())
                    .price(album.getPrice())
                    .quantity(1)
                    .build();
            cartItems.add(newItem);
        }
    }

    @Override
    public void removeFromCart(HttpSession session, UUID albumId) {

        List<CartItemDTO> cartItems = getCartItems(session);
        cartItems.removeIf(item -> item.getAlbumId().equals(albumId));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CartItemDTO> getCartItems(HttpSession session) {

        return (List<CartItemDTO>) session.getAttribute(MODEL_CART);
    }

    @Override
    public void clearCart(HttpSession session) {

        session.setAttribute(MODEL_CART, new ArrayList<CartItemDTO>());
    }

    @Override
    public int getCartItemCount(HttpSession session) {

        List<CartItemDTO> cartItems = getCartItems(session);
        if (cartItems == null) {
            return 0;
        }

        return cartItems.stream()
                .mapToInt(CartItemDTO::getQuantity)
                .sum();
    }

    @Override
    public BigDecimal getCartTotal(HttpSession session) {

        List<CartItemDTO> cartItems = getCartItems(session);
        if (cartItems == null) {
            return BigDecimal.ZERO;
        }

        return cartItems.stream()
                .map(CartItemDTO::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
