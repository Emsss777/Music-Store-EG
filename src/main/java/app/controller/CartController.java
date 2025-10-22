package app.controller;

import app.model.dto.CartItemDTO;
import app.model.entity.Album;
import app.service.AlbumService;
import app.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static app.util.FlashAttributes.*;
import static app.util.ModelAttributes.*;
import static app.util.Redirects.REDIRECT_CART;
import static app.util.SuccessMessages.*;
import static app.util.UrlParams.PARAM_ID;
import static app.util.UrlPaths.*;
import static app.util.Views.VIEW_CART;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class CartController {

    private final CartService cartService;
    private final AlbumService albumService;

    @GetMapping(URL_CART)
    public ModelAndView getCartPage(HttpSession session) {

        initializeCart(session);

        List<CartItemDTO> cartItems = cartService.getCartItems(session);
        BigDecimal cartTotal = cartService.getCartTotal(session);
        int itemCount = cartService.getCartItemCount(session);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_CART);
        modelAndView.addObject(MODEL_PAGE, VIEW_CART);
        modelAndView.addObject(MODEL_CART_ITEMS, cartItems);
        modelAndView.addObject(MODEL_CART_TOTAL, cartTotal);
        modelAndView.addObject(MODEL_ITEM_COUNT, itemCount);

        return modelAndView;
    }

    @PostMapping(URL_CART + URL_ADD + PARAM_ID)
    public ModelAndView addToCart(@PathVariable UUID id, HttpSession session,
                                  RedirectAttributes redirectAttributes) {

        initializeCart(session);

        Album album = albumService.getAlbumById(id);
        cartService.addToCart(session, album);

        redirectAttributes.addFlashAttribute(FLASH_MESSAGE, ALBUM_ADDED_TO_CART);

        return new ModelAndView(REDIRECT_CART);
    }

    @PostMapping(URL_CART + URL_REMOVE + PARAM_ID)
    public ModelAndView removeFromCart(@PathVariable UUID id, HttpSession session,
                                       RedirectAttributes redirectAttributes) {

        initializeCart(session);

        cartService.removeFromCart(session, id);
        redirectAttributes.addFlashAttribute(FLASH_MESSAGE, ALBUM_REMOVED_FROM_CART);

        return new ModelAndView(REDIRECT_CART);
    }

    @PostMapping(URL_CART + URL_CLEAR)
    public ModelAndView clearCart(HttpSession session, RedirectAttributes redirectAttributes) {

        cartService.clearCart(session);
        redirectAttributes.addFlashAttribute(FLASH_MESSAGE, CART_CLEARED);

        return new ModelAndView(REDIRECT_CART);
    }

    private void initializeCart(HttpSession session) {

        if (session.getAttribute(VIEW_CART) == null) {
            session.setAttribute(VIEW_CART, new ArrayList<CartItemDTO>());
        }
    }
}
