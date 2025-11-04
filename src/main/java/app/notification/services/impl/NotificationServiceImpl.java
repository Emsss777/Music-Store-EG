package app.notification.services.impl;

import app.exception.NotificationServiceFeignCallException;
import app.notification.client.dto.Notification;
import app.notification.client.dto.NotificationPreference;
import app.notification.client.dto.NotificationRequest;
import app.notification.services.NotificationService;
import app.notification.client.NotificationClient;
import app.notification.client.dto.UpsertNotificationPreference;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;

import java.util.List;
import java.util.UUID;

import static app.util.LogMessages.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationClient notificationClient;

    @Value("${notification-svc.failure-message.clear-history}")
    private String clearHistoryFailedMessage;

    @Override
    public void saveNotificationPreference(UUID userId, boolean isEmailEnabled, String email) {

        UpsertNotificationPreference upsert =
                UpsertNotificationPreference.toUpsert(userId, isEmailEnabled, email);

        try {
            ResponseEntity<Void> httpResponse = notificationClient.upsertNotificationPreference(upsert);
            if (!httpResponse.getStatusCode().is2xxSuccessful()) {
                log.error(AnsiOutput.toString(AnsiColor.BRIGHT_MAGENTA, NOTIFICATION_SAVE_PREF_NON_2XX),
                        userId, httpResponse.getStatusCode());
            }
        } catch (FeignException ex) {
            log.warn(AnsiOutput.toString(AnsiColor.BRIGHT_MAGENTA, NOTIFICATION_SAVE_PREF_ERROR),
                    userId, ex.status(), ex.getMessage());
        } catch (Exception ex) {
            log.error(AnsiOutput.toString(AnsiColor.BRIGHT_MAGENTA, NOTIFICATION_SAVE_PREF_UNEXPECTED), userId, ex);
        }
    }

    @Override
    public NotificationPreference getNotificationPreference(UUID userId) {

        try {
            ResponseEntity<NotificationPreference> httpResponse = notificationClient.getUserPreference(userId);
            if (httpResponse.getStatusCode().is2xxSuccessful() && httpResponse.getBody() != null) {
                return httpResponse.getBody();
            }
            log.warn(AnsiOutput.toString(AnsiColor.BRIGHT_MAGENTA, NOTIFICATION_GET_PREF_NON_2XX),
                    userId, httpResponse.getStatusCode());

        } catch (FeignException ex) {
            log.warn(AnsiOutput.toString(AnsiColor.BRIGHT_MAGENTA, NOTIFICATION_GET_PREF_ERROR),
                    userId, ex.status(), ex.getMessage());
        } catch (Exception ex) {
            log.error(AnsiOutput.toString(AnsiColor.BRIGHT_MAGENTA, NOTIFICATION_GET_PREF_UNEXPECTED), userId, ex);
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
            log.warn(AnsiOutput.toString(AnsiColor.BRIGHT_MAGENTA, NOTIFICATION_GET_HISTORY_NON_2XX),
                    userId, httpResponse.getStatusCode());

        } catch (FeignException ex) {
            log.warn(AnsiOutput.toString(AnsiColor.BRIGHT_MAGENTA, NOTIFICATION_GET_HISTORY_ERROR),
                    userId, ex.status(), ex.getMessage());
        } catch (Exception ex) {
            log.error(AnsiOutput.toString(AnsiColor.BRIGHT_MAGENTA, NOTIFICATION_GET_HISTORY_UNEXPECTED), userId, ex);
        }
        return Notification.defaultHistory();
    }

    @Override
    public void sendNotification(UUID userId, String emailSubject, String emailBody) {

        NotificationRequest notificationRequest = NotificationRequest.defaultFor(userId, emailSubject, emailBody);

        ResponseEntity<Void> httpResponse;
        try {
            httpResponse = notificationClient.sendNotification(notificationRequest);
            if (!httpResponse.getStatusCode().is2xxSuccessful()) {
                log.error(AnsiOutput.toString(AnsiColor.BRIGHT_MAGENTA, NOTIFICATION_SEND_EMAIL_ERROR), userId);
            }
        } catch (Exception e) {
            log.warn(AnsiOutput.toString(AnsiColor.BRIGHT_MAGENTA, NOTIFICATION_SEND_EMAIL_NON_2XX), userId);
        }
    }

    @Override
    public void updateNotificationPreference(UUID userId, boolean enabled) {

        try {
            notificationClient.updateNotificationPreference(userId, enabled);
        } catch (Exception e) {
            log.warn(AnsiOutput.toString(AnsiColor.BRIGHT_MAGENTA, NOTIFICATION_UPDATE_PREF_ERROR), userId);
        }
    }

    @Override
    public void clearHistory(UUID userId) {

        try {
            notificationClient.clearHistory(userId);
        } catch (Exception e) {
            log.error(AnsiOutput.toString(AnsiColor.BRIGHT_MAGENTA, NOTIFICATION_CLEAR_HISTORY_ERROR), userId);
            throw new NotificationServiceFeignCallException(clearHistoryFailedMessage);
        }
    }

    @Override
    public void retryFailed(UUID userId) {

        try {
            notificationClient.retryFailedNotifications(userId);
        } catch (Exception e) {
            log.error(AnsiOutput.toString(AnsiColor.BRIGHT_MAGENTA, NOTIFICATION_CLEAR_HISTORY_ERROR), userId);
            throw new NotificationServiceFeignCallException(clearHistoryFailedMessage);
        }
    }
}
