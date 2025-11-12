package app.service.impl;

import app.model.dto.CartItemDTO;
import app.model.dto.CartSummaryDTO;
import app.model.entity.Album;
import app.model.entity.Artist;
import app.service.AlbumService;
import app.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static app.TestDataFactory.aCartItem;
import static app.TestDataFactory.anAlbum;
import static app.TestDataFactory.anArtist;
import static app.util.ModelAttributes.MODEL_CART;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private AlbumService albumService;

    private CartService cartService;
    private HttpSession session;

    @BeforeEach
    void setUp() {

        cartService = new CartServiceImpl(albumService);
        session = new MockHttpSession();
    }

    @Test
    void addToCart_whenAlbumNotPresent_shouldAddNewCartItem() {

        UUID albumId = UUID.randomUUID();
        Artist artist = anArtist();
        Album album = anAlbum(artist);
        album.setId(albumId);
        when(albumService.getAlbumById(albumId)).thenReturn(album);

        cartService.addToCart(session, albumId);

        List<CartItemDTO> cartItems = getCartItems();
        assertThat(cartItems).hasSize(1);
        CartItemDTO item = cartItems.get(0);
        assertThat(item.getAlbumId()).isEqualTo(albumId);
        assertThat(item.getQuantity()).isEqualTo(1);
        assertThat(item.getTitle()).isEqualTo(album.getTitle());
    }

    @Test
    void addToCart_whenAlbumAlreadyInCart_shouldIncreaseQuantity() {

        UUID albumId = UUID.randomUUID();
        Artist artist = anArtist();
        Album album = anAlbum(artist);
        album.setId(albumId);
        CartItemDTO existingItem = CartItemDTO.builder()
                .albumId(albumId)
                .title(album.getTitle())
                .artistName(album.getArtist().getArtistName())
                .coverUrl(album.getCoverUrl())
                .price(album.getPrice())
                .quantity(1)
                .build();
        session.setAttribute(MODEL_CART, new ArrayList<>(List.of(existingItem)));
        when(albumService.getAlbumById(albumId)).thenReturn(album);

        cartService.addToCart(session, albumId);

        List<CartItemDTO> cartItems = getCartItems();
        assertThat(cartItems).hasSize(1);
        assertThat(cartItems.get(0).getQuantity()).isEqualTo(2);
    }

    @Test
    void removeFromCart_whenAlbumPresent_shouldRemoveCartItem() {

        UUID albumId = UUID.randomUUID();
        Artist artist = anArtist();
        Album album = anAlbum(artist);
        album.setId(albumId);
        session.setAttribute(MODEL_CART, new ArrayList<>(List.of(aCartItem(album))));

        cartService.removeFromCart(session, albumId);

        List<CartItemDTO> cartItems = getCartItems();
        assertThat(cartItems).isEmpty();
    }

    @Test
    void clearCart_shouldRemoveAllItemsAndResetSummary() {

        UUID albumId = UUID.randomUUID();
        Artist artist = anArtist();
        Album album = anAlbum(artist);
        album.setId(albumId);
        session.setAttribute(MODEL_CART, new ArrayList<>(List.of(aCartItem(album))));

        cartService.clearCart(session);

        List<CartItemDTO> cartItems = getCartItems();
        assertThat(cartItems).isNotNull().isEmpty();
    }

    @Test
    void getCartTotal_withItems_shouldReturnAggregatedPrice() {

        UUID albumId = UUID.randomUUID();
        Artist artist = anArtist();
        Album album = anAlbum(artist);
        album.setId(albumId);
        CartItemDTO firstItem = aCartItem(album);
        firstItem.setQuantity(2);
        CartItemDTO secondItem = aCartItem(album);
        secondItem.setQuantity(1);
        session.setAttribute(MODEL_CART, new ArrayList<>(List.of(firstItem, secondItem)));

        BigDecimal total = cartService.getCartTotal(session);

        assertThat(total).isEqualByComparingTo(new BigDecimal("59.97"));
        CartSummaryDTO summary = cartService.getCartSummary(session);
        assertThat(summary.getTotal()).isEqualByComparingTo(total);
        assertThat(summary.getItemCount()).isEqualTo(3);
    }

    @SuppressWarnings("unchecked")
    private List<CartItemDTO> getCartItems() {

        Object attribute = session.getAttribute(MODEL_CART);
        if (attribute == null) {
            return List.of();
        }
        return (List<CartItemDTO>) attribute;
    }
}
