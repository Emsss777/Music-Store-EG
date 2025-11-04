package app.notification.services;

import app.notification.client.dto.Notification;
import app.notification.client.dto.NotificationPreference;

import java.util.List;
import java.util.UUID;

public interface NotificationService {

    void saveNotificationPreference(UUID userId, boolean isEmailEnabled, String email);

    NotificationPreference getNotificationPreference(UUID userId);

    List<Notification> getNotificationHistory(UUID userId);

    void sendNotification(UUID userId, String emailSubject, String emailBody);

    void updateNotificationPreference(UUID userId, boolean enabled);

    void clearHistory(UUID userId);

    void retryFailed(UUID userId);
}
