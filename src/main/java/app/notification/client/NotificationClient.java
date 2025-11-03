package app.notification.client;

import app.notification.client.dto.Notification;
import app.notification.client.dto.NotificationPreference;
import app.notification.client.dto.NotificationRequest;
import app.notification.client.dto.UpsertNotificationPreference;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

import static app.util.FeignClients.*;
import static app.util.UrlPaths.URL_PREFERENCES;

@FeignClient(name = NOTIFICATION_SVC, url = NOTIFICATION_SVC_URL)
public interface NotificationClient {

    @PostMapping(URL_PREFERENCES)
    ResponseEntity<Void> upsertNotificationPreference(
            @RequestBody UpsertNotificationPreference notificationPreference);

    @GetMapping(URL_PREFERENCES)
    ResponseEntity<NotificationPreference> getUserPreference(@RequestParam(name = "userId") UUID userId);

    @GetMapping
    ResponseEntity<List<Notification>> getNotificationHistory(@RequestParam(name = "userId") UUID userId);

    @PostMapping
    ResponseEntity<Void> sendNotification(@RequestBody NotificationRequest notificationRequest);
}
