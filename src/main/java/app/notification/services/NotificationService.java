package app.notification.services;

import app.notification.client.dto.NotificationDTO;
import app.notification.client.dto.NotificationPreferenceDTO;

import java.util.List;
import java.util.UUID;

public interface NotificationService {

    void saveNotificationPreference(UUID userId, boolean isEmailEnabled, String email);

    NotificationPreferenceDTO getNotificationPreference(UUID userId);

    List<NotificationDTO> getNotificationHistory(UUID userId);

    void sendNotification(UUID userId, String emailSubject, String emailBody);

    void updateNotificationPreference(UUID userId, boolean enabled);

    void clearHistory(UUID userId);

    void clearAllHistory();

    void retryFailed(UUID userId);
}
