package app.notification.services.impl;

import app.notification.client.dto.Notification;
import app.notification.client.dto.NotificationPreference;
import app.notification.services.NotificationService;
import app.notification.client.NotificationClient;
import app.notification.client.dto.UpsertNotificationPreference;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
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

        UpsertNotificationPreference upsert =
                UpsertNotificationPreference.toUpsert(userId, isEmailEnabled, email);

        try {
            ResponseEntity<Void> httpResponse = notificationClient.upsertNotificationPreference(upsert);
            if (!httpResponse.getStatusCode().is2xxSuccessful()) {
                log.error(NOTIFICATION_SAVE_PREF_NON_2XX, userId, httpResponse.getStatusCode());
            }
        } catch (FeignException ex) {
            log.warn(NOTIFICATION_SAVE_PREF_ERROR, userId, ex.status(), ex.getMessage());
        } catch (Exception ex) {
            log.error(NOTIFICATION_SAVE_PREF_UNEXPECTED, userId, ex);
        }
    }

    @Override
    public NotificationPreference getNotificationPreference(UUID userId) {

        try {
            ResponseEntity<NotificationPreference> httpResponse = notificationClient.getUserPreference(userId);
            if (httpResponse.getStatusCode().is2xxSuccessful() && httpResponse.getBody() != null) {
                return httpResponse.getBody();
            }
            log.warn(NOTIFICATION_GET_PREF_NON_2XX, userId, httpResponse.getStatusCode());

        } catch (FeignException ex) {
            log.warn(NOTIFICATION_GET_PREF_ERROR, userId, ex.status(), ex.getMessage());
        } catch (Exception ex) {
            log.error(NOTIFICATION_GET_PREF_UNEXPECTED, userId, ex);
        }
        return NotificationPreference.defaultFor(userId);
    }

    @Override
    public List<Notification> getNotificationHistory(UUID userId) {

        try {
            ResponseEntity<List<Notification>> httpResponse = notificationClient.getNotificationHistory(userId);

            if (httpResponse.getStatusCode().is2xxSuccessful() && httpResponse.getBody() != null) {
                return httpResponse.getBody();
            }
            log.warn(NOTIFICATION_GET_HISTORY_NON_2XX, userId, httpResponse.getStatusCode());

        } catch (FeignException ex) {
            log.warn(NOTIFICATION_GET_HISTORY_ERROR, userId, ex.status(), ex.getMessage());
        } catch (Exception ex) {
            log.error(NOTIFICATION_GET_HISTORY_UNEXPECTED, userId, ex);
        }
        return Notification.defaultHistory();
    }
}
