package app.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LogMessages {

    public static final String USER_CREATED =
            "Successfully created new User account for username [{}] and ID [{}]!";
    public static final String USER_REGISTERED_EVENT_PUBLISHED =
            "Successfully published registered event for user [{}]!";
    public static final String USERNAME_DOES_NOT_EXIST = "Username [{}] does NOT Exist!";
    public static final String ADMIN_ALREADY_EXISTS = "Admin User [{}] Already Exists! Skipping Seeding!";
    public static final String ADMIN_NOT_FOUND = "Admin User [{}] NOT Found! Creating new...";
    public static final String ARTIST_ALREADY_EXISTS = "Artist [{}] Already Exists! Skipping Seeding!";
    public static final String ARTIST_NOT_FOUND = "Artist [{}] NOT Found! Creating new...";
    public static final String ALBUM_ALREADY_EXISTS = "Album [{}] Already Exists! Skipping Seeding!";
    public static final String ALBUM_NOT_FOUND = "Album [{}] NOT Found! Creating new...";
    public static final String TEST_USER_ALREADY_EXISTS = "Test User [{}] Already Exists! Skipping Seeding!";
    public static final String TEST_USER_NOT_FOUND = "Test User [{}] NOT Found! Creating new...";
    public static final String SEEDED_ADMIN_USER = "Seeded Admin User [{}]!";
    public static final String SEEDED_TEST_USER = "Seeded Test User [{}]!";
    public static final String SEEDED_ARTIST = "Seeded Artist [{}]!";
    public static final String SEEDED_ALBUM = "Seeded Album [{}]!";
    public static final String SEEDING_COMPLETE = "Seeding Complete!";
    public static final String SEEDING = "Seeding {}!";
    public static final String BEFORE_METHOD_EXEC = "Before method execution!";
    public static final String AFTER_METHOD_EXEC = "After method execution!";
    public static final String ANOTHER_METHOD_EXEC = "Hey, another method in UserController was executed!";
    public static final String NOTIFICATION_SAVE_PREF_NON_2XX =
            "[notification-svc] non-2xx on 'saveNotificationPreference' for user [{}]: {}!";
    public static final String NOTIFICATION_SAVE_PREF_ERROR =
            "[notification-svc] error on 'saveNotificationPreference' for user [{}]: status={}, msg={}!";
    public static final String NOTIFICATION_SAVE_PREF_UNEXPECTED =
            "[notification-svc] unexpected error on 'saveNotificationPreference' for user [{}]!";
    public static final String NOTIFICATION_GET_PREF_NON_2XX =
            "[notification-svc] non-2xx on 'getNotificationPreference' for user [{}]: {}!";
    public static final String NOTIFICATION_GET_PREF_ERROR =
            "[notification-svc] error on 'getNotificationPreference' for user [{}]: status={}, msg={}!";
    public static final String NOTIFICATION_GET_PREF_UNEXPECTED =
            "[notification-svc] unexpected error on 'getNotificationPreference' for user [{}]!";
    public static final String NOTIFICATION_GET_HISTORY_NON_2XX =
            "[notification-svc] non-2xx on 'getNotificationHistory' for user [{}]: {}!";
    public static final String NOTIFICATION_GET_HISTORY_ERROR =
            "[notification-svc] error on 'getNotificationHistory' for user [{}]: status={}, msg={}!";
    public static final String NOTIFICATION_GET_HISTORY_UNEXPECTED =
            "[notification-svc] unexpected error on 'getNotificationHistory' for user [{}]!";
    public static final String NOTIFICATION_SEND_EMAIL_ERROR =
            "[notification-svc] error on 'sendEmail' for user [{}]!";
    public static final String NOTIFICATION_SEND_EMAIL_NON_2XX =
            "[notification-svc] non-2xx on 'sendEmail' for user [{}]: 500 Internal Server Error!";
    public static final String NOTIFICATION_UPDATE_PREF_ERROR =
            "[notification-svc] error on 'updateNotificationPreference' for user [{}]!";
    public static final String NOTIFICATION_CLEAR_HISTORY_ERROR =
            "[notification-svc] error on 'clearNotificationHistory' for user [{}]!";
    public static final String ALBUM_DELETED_WITH_DETAILS =
            "Deleted album [%s] with %d order items and %d empty orders removed!";
    public static final String ORDER_AUTO_CANCELLED =
            "Order {} with status PENDING older than 24 hours has been auto-canceled!";
    public static final String ORDER_AUTO_CANCELLED_COUNT =
            "Processed {} pending orders for auto-cancellation!";
    public static final String ORDER_AUTO_SHIPPED =
            "Order {} with status PAID older than 2 hours has been moved to SHIPPED!";
    public static final String ORDER_AUTO_SHIPPED_COUNT =
            "Processed {} paid orders for shipping!";
    public static final String ORDER_AUTO_COMPLETED =
            "Order {} with status SHIPPED older than 72 hours has been moved to COMPLETED!";
    public static final String ORDER_AUTO_COMPLETED_COUNT =
            "Processed {} shipped orders for completion!";
    public static final String ORDER_NO_STATUS_UPDATES =
            "No orders found for status update!";
    public static final String CART_ALBUM_QUANTITY_INCREASED =
            "Increased quantity for album [{}] in cart. New quantity: {}.";
    public static final String CART_ALBUM_ADDED =
            "Album [{}] added to cart successfully!";
    public static final String CART_ALBUM_REMOVED =
            "Album [{}] removed from cart!";
    public static final String CART_CLEARED =
            "Cart cleared! Removed {} item(s)!";
    public static final String ORDER_CREATED =
            "Order created successfully! Order Number: [{}], User: [{}], Total Amount: [{}], Items: [{}]!";
    public static final String ORDER_ITEMS_DELETED =
            "Deleted {} order item(s) for album [{}]!";
    public static final String ORDERS_EMPTY_DELETED =
            "Deleted {} empty order(s)!";
    public static final String USER_DETAILS_UPDATED =
            "User details updated for user [{}] with ID [{}]!";
    public static final String USER_STATUS_CHANGED =
            "User [{}] status changed from [{}] to [{}]!";
    public static final String USER_ROLE_CHANGED =
            "User [{}] role changed from [{}] to [{}]!";

}
