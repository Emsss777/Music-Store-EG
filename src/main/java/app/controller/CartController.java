package app.controller;

import app.model.dto.CartItemDTO;
import app.model.entity.AlbumEntity;
import app.service.AlbumService;
import app.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
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

import static app.util.ModelAttributes.*;
import static app.util.Redirects.REDIRECT_CART;
import static app.util.UrlParams.PARAM_ID;
import static app.util.UrlPaths.URL_CART;
import static app.util.Views.VIEW_CART;

@Controller
@RequiredArgsConstructor
public class CartController {

    private static final String CART_SESSION_KEY = "cart";

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
        modelAndView.addObject("cartItems", cartItems);
        modelAndView.addObject("cartTotal", cartTotal);
        modelAndView.addObject("itemCount", itemCount);

        return modelAndView;
    }

    @PostMapping(URL_CART + "/add" + PARAM_ID)
    public String addToCart(@PathVariable UUID id, HttpSession session,
                            RedirectAttributes redirectAttributes) {

        initializeCart(session);

        AlbumEntity album = albumService.getAlbumById(id);
        cartService.addToCart(session, album);

        redirectAttributes.addFlashAttribute("message", "Album added to cart successfully!");

        return REDIRECT_CART;
    }

    @PostMapping(URL_CART + "/remove" + PARAM_ID)
    public String removeFromCart(@PathVariable UUID id, HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        initializeCart(session);

        cartService.removeFromCart(session, id);
        redirectAttributes.addFlashAttribute("message", "Album removed from cart.");

        return REDIRECT_CART;
    }

    @PostMapping(URL_CART + "/clear")
    public String clearCart(HttpSession session, RedirectAttributes redirectAttributes) {

        cartService.clearCart(session);
        redirectAttributes.addFlashAttribute("message", "Cart cleared.");

        return REDIRECT_CART;
    }

    private void initializeCart(HttpSession session) {

        if (session.getAttribute(CART_SESSION_KEY) == null) {
            session.setAttribute(CART_SESSION_KEY, new ArrayList<CartItemDTO>());
        }
    }
}
