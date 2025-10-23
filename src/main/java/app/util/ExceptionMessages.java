package app.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ExceptionMessages {

    public static final String BAD_CREDENTIALS = "Bad Credentials!";
    public static final String USERNAME_DOES_NOT_EXIST = "Username [{}] does NOT Exist!";
    public static final String USER_DOES_NOT_EXIST = "User [%s] does NOT Exist!";
    public static final String USERNAME_ALREADY_EXIST = "Username [%s] Already Exist!";
    public static final String ADMIN_ALREADY_EXIST = "Admin User [{}] Already Exists! Skipping Seeding!";
    public static final String ADMIN_NOT_FOUND = "Admin User [{}] NOT Found! Creating new...";
    public static final String ARTIST_DOES_NOT_EXIST = "Artist [%s] does NOT Exist!";
    public static final String ARTIST_ALREADY_EXIST = "Artist [{}] Already Exists! Skipping Seeding!";
    public static final String ARTIST_NOT_FOUND = "Artist [{}] NOT Found! Creating new...";
    public static final String ALBUM_DOES_NOT_EXIST = "Album [%s] does NOT Exist!";
    public static final String ALBUM_ALREADY_EXIST = "Album [{}] Already Exists! Skipping Seeding!";
    public static final String ALBUM_NOT_FOUND = "Album [{}] NOT Found! Creating new...";
    public static final String TEST_USER_ALREADY_EXIST = "Test User [{}] Already Exists! Skipping Seeding!";
    public static final String TEST_USER_NOT_FOUND = "Test User [{}] NOT Found! Creating new...";
    public static final String INCORRECT_USERNAME_OR_PASSWORD = "Incorrect Username or Password!";
    public static final String USER_CANNOT_BE_EMPTY = "Username cannot be empty!";
    public static final String PASSWORD_CANNOT_BE_EMPTY = "Password cannot be empty!";
    public static final String PASSWORD_DO_NOT_MATCH = "Passwords do NOT Match!";
    public static final String USERNAME_INVALID_LENGTH = "Username length must be between 3 and 20 characters!";
    public static final String PASSWORD_INVALID_LENGTH = "Password length must be between 3 and 20 characters!";
    public static final String SELECT_A_COUNTRY = "You must select a country!";
    public static final String FIRST_NAME_INVALID_LENGTH = "First name cannot exceed 20 characters!";
    public static final String LAST_NAME_INVALID_LENGTH = "Last name cannot exceed 20 characters!";
    public static final String INVALID_EMAIL_FORMAT = "Email must be in a valid format!";
    public static final String BIO_INVALID_LENGTH = "Bio cannot exceed 500 characters!";
    public static final String INVALID_URL_FORMAT = "Profile picture must be a valid URL!";
    public static final String CART_IS_EMPTY = "Your cart is empty!";
    public static final String ALBUM_TITLE_TOO_LONG = "Album title must not exceed 100 characters!";
    public static final String ALBUM_YEAR_TOO_HIGH = "Release year must not exceed 2025";
    public static final String ALBUM_DESCRIPTION_TOO_LONG = "Description must not exceed 1000 characters!";
    public static final String ALBUM_COVER_URL_INVALID = "Cover URL must be a valid HTTP/HTTPS URL!";
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
}
