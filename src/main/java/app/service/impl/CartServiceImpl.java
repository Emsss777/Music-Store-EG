package app.service.impl;

import app.mapper.CartItemMapper;
import app.model.dto.CartItemDTO;
import app.model.dto.CartSummaryDTO;
import app.model.entity.Album;
import app.service.AlbumService;
import app.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static app.util.LogMessages.*;
import static app.util.ModelAttributes.MODEL_CART;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final AlbumService albumService;

    @Override
    @Transactional
    public void addToCart(HttpSession session, UUID albumId) {

        List<CartItemDTO> cartItems = this.getOrInitializeCart(session);
        Album album = albumService.getAlbumById(albumId);

        cartItems.stream()
                .filter(item -> item.getAlbumId().equals(album.getId()))
                .findFirst()
                .ifPresentOrElse(
                        item -> {
                            item.setQuantity(item.getQuantity() + 1);
                            log.info(AnsiOutput.toString(
                                    AnsiColor.BRIGHT_GREEN, CART_ALBUM_QUANTITY_INCREASED),
                                    album.getTitle(), item.getQuantity());
                        },
                        () -> {
                            cartItems.add(CartItemMapper.fromAlbum(album));
                            log.info(AnsiOutput.toString(
                                    AnsiColor.BRIGHT_GREEN, CART_ALBUM_ADDED), album.getTitle());
                        }
                );

        session.setAttribute(MODEL_CART, cartItems);
    }

    @Override
    public void removeFromCart(HttpSession session, UUID albumId) {

        List<CartItemDTO> cartItems = this.getCartItems(session);
        boolean removed = cartItems.removeIf(item -> item.getAlbumId().equals(albumId));

        if (removed) {
            session.setAttribute(MODEL_CART, cartItems);
            log.info(AnsiOutput.toString(AnsiColor.BRIGHT_GREEN, CART_ALBUM_REMOVED), albumId);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CartItemDTO> getCartItems(HttpSession session) {

        return (List<CartItemDTO>) session.getAttribute(MODEL_CART);
    }

    @Override
    public void clearCart(HttpSession session) {

        List<CartItemDTO> cartItems = this.getCartItems(session);
        int itemCount = cartItems != null ? cartItems.size() : 0;

        session.setAttribute(MODEL_CART, new ArrayList<CartItemDTO>());
        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_GREEN, CART_CLEARED), itemCount);
    }

    @Override
    public int getCartItemCount(HttpSession session) {

        List<CartItemDTO> cartItems = this.getCartItems(session);
        if (cartItems == null) {
            return 0;
        }

        return cartItems.stream()
                .mapToInt(CartItemDTO::getQuantity)
                .sum();
    }

    @Override
    public BigDecimal getCartTotal(HttpSession session) {

        List<CartItemDTO> cartItems = this.getCartItems(session);
        if (cartItems == null) {
            return BigDecimal.ZERO;
        }

        return cartItems.stream()
                .map(CartItemDTO::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CartItemDTO> getOrInitializeCart(HttpSession session) {

        List<CartItemDTO> cartItems = (List<CartItemDTO>) session.getAttribute(MODEL_CART);

        if (cartItems == null) {
            cartItems = new ArrayList<>();
            session.setAttribute(MODEL_CART, cartItems);
        }

        return cartItems;
    }

    @Override
    public CartSummaryDTO getCartSummary(HttpSession session) {

        List<CartItemDTO> items = this.getOrInitializeCart(session);
        return CartSummaryDTO.of(items);
    }
}
