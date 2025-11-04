package app.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SuccessMessages {

    public static final String ALBUM_ADDED_TO_CART = "Album added to cart successfully!";
    public static final String ALBUM_REMOVED_FROM_CART = "Album removed from cart!";
    public static final String ALBUM_DELETED = "Album deleted successfully!";
    public static final String CART_CLEARED = "Cart cleared!";
    public static final String ORDER_PLACED_SUCCESS = "Order placed successfully! Order Number: ";
    public static final String ORDER_CONFIRMATION = "Order Confirmation";
    public static final String NOTIFICATION_ORDER_CONFIRMATION =
            "Dear %s, your order #%s has been successfully created! Total amount: %s";
}
