package app.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SuccessMessages {

    public static final String USER_CREATED =
            "Successfully created new User account for username [{}] and ID [{}]";
    public static final String USER_REGISTERED_EVENT_PUBLISHED =
            "Successfully published registered event for user [{}]";
    public static final String SEEDED_ADMIN_USER = "Seeded Admin User [{}].";
    public static final String SEEDED_TEST_USER = "Seeded Test User [{}].";
    public static final String SEEDED_ARTIST = "Seeded Artist [{}].";
    public static final String SEEDED_ALBUM = "Seeded Album [{}].";
    public static final String SEEDING_COMPLETE = "Seeding Complete!";
    public static final String SEEDING = "Seeding {}";
    public static final String BEFORE_METHOD_EXEC = "Before method execution!";
    public static final String AFTER_METHOD_EXEC = "After method execution!";
    public static final String ANOTHER_METHOD_EXEC = "Hey, another method in UserController was executed!";
    public static final String ALBUM_ADDED_TO_CART = "Album added to cart successfully!";
    public static final String ALBUM_REMOVED_FROM_CART = "Album removed from cart!";
    public static final String ALBUM_DELETED = "Album deleted successfully!";
    public static final String ALBUM_DELETED_WITH_DETAILS =
            "Deleted album [%s] with %d order items and %d empty orders removed!";
    public static final String CART_CLEARED = "Cart cleared!";
    public static final String ORDER_PLACED_SUCCESS = "Order placed successfully! Order Number: ";
    public static final String ORDER_CONFIRMATION = "Order Confirmation";
    public static final String NOTIFICATION_ORDER_CONFIRMATION =
            "Dear %s, your order #%s has been successfully created! Total amount: %s";
}
