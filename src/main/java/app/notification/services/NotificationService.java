package app.notification.services;

import java.util.UUID;

public interface NotificationService {

    void saveNotificationPreference(UUID userId, boolean isEmailEnabled, String email);
}
