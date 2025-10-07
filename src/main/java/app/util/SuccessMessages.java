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
    public static final String SEEDING_COMPLETE = "Seeding Complete!";
    public static final String SEEDING = "Seeding {}";
    public static final String BEFORE_METHOD_EXEC = "Before method execution!";
    public static final String AFTER_METHOD_EXEC = "After method execution!";
    public static final String ANOTHER_METHOD_EXEC = "Hey, another method in UserController was executed!";
}
