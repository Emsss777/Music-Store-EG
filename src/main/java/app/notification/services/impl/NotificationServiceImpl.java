package app.notification.services.impl;

import app.notification.services.NotificationService;
import app.notification.client.NotificationClient;
import app.notification.client.dto.UpsertNotificationPreference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static app.util.ExceptionMessages.*;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationClient notificationClient;

    @Autowired
    public NotificationServiceImpl(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    @Override
    public void saveNotificationPreference(UUID userId, boolean isEmailEnabled, String email) {

        UpsertNotificationPreference notificationPreference = UpsertNotificationPreference.builder()
                .userId(userId)
                .contactInfo(email)
                .type("EMAIL")
                .notificationEnabled(isEmailEnabled)
                .build();

        try {
            ResponseEntity<Void> httpResponse = notificationClient
                    .upsertNotificationPreference(notificationPreference);

            if (!httpResponse.getStatusCode().is2xxSuccessful()) {
                log.error(FEIGN_NOTIFICATION_SAVE_FAILED, userId);
            }
        } catch (Exception e) {
            log.error(FEIGN_NOTIFICATION_CALL_FAILED);
        }
    }
}
