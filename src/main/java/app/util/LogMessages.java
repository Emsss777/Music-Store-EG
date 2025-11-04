package app.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LogMessages {

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
    public static final String NOTIFICATION_SAVE_PREF_NON_2XX =
            "[notification-svc] non-2xx on 'saveNotificationPreference' for user [{}]: {}";
    public static final String NOTIFICATION_SAVE_PREF_ERROR =
            "[notification-svc] error on 'saveNotificationPreference' for user [{}]: status={}, msg={}";
    public static final String NOTIFICATION_SAVE_PREF_UNEXPECTED =
            "[notification-svc] unexpected error on 'saveNotificationPreference' for user [{}]";
    public static final String NOTIFICATION_GET_PREF_NON_2XX =
            "[notification-svc] non-2xx on 'getNotificationPreference' for user [{}]: {}";
    public static final String NOTIFICATION_GET_PREF_ERROR =
            "[notification-svc] error on 'getNotificationPreference' for user [{}]: status={}, msg={}";
    public static final String NOTIFICATION_GET_PREF_UNEXPECTED =
            "[notification-svc] unexpected error on 'getNotificationPreference' for user [{}]";
    public static final String NOTIFICATION_GET_HISTORY_NON_2XX =
            "[notification-svc] non-2xx on 'getNotificationHistory' for user [{}]: {}";
    public static final String NOTIFICATION_GET_HISTORY_ERROR =
            "[notification-svc] error on 'getNotificationHistory' for user [{}]: status={}, msg={}";
    public static final String NOTIFICATION_GET_HISTORY_UNEXPECTED =
            "[notification-svc] unexpected error on 'getNotificationHistory' for user [{}]";
    public static final String NOTIFICATION_SEND_EMAIL_ERROR =
            "[notification-svc] error on 'sendEmail' for user [{}]";
    public static final String NOTIFICATION_SEND_EMAIL_NON_2XX =
            "[notification-svc] non-2xx on 'sendEmail' for user [{}]: 500 Internal Server Error";
    public static final String NOTIFICATION_UPDATE_PREF_ERROR =
            "[notification-svc] error on 'updateNotificationPreference' for user [{}]";
    public static final String NOTIFICATION_CLEAR_HISTORY_ERROR =
            "[notification-svc] error on 'clearNotificationHistory' for user [{}]";
    public static final String ALBUM_DELETED_WITH_DETAILS =
            "Deleted album [%s] with %d order items and %d empty orders removed!";
    public static final String ORDER_AUTO_CANCELLED =
            "Order {} with status PENDING older than 24 hours has been auto-canceled.";
    public static final String ORDER_AUTO_CANCELLED_COUNT =
            "Processed {} pending orders for auto-cancellation.";
    public static final String ORDER_AUTO_SHIPPED =
            "Order {} with status PAID older than 2 hours has been moved to SHIPPED.";
    public static final String ORDER_AUTO_SHIPPED_COUNT =
            "Processed {} paid orders for shipping.";
    public static final String ORDER_AUTO_COMPLETED =
            "Order {} with status SHIPPED older than 72 hours has been moved to COMPLETED.";
    public static final String ORDER_AUTO_COMPLETED_COUNT =
            "Processed {} shipped orders for completion.";
    public static final String ORDER_NO_STATUS_UPDATES =
            "No orders found for status update.";
}
