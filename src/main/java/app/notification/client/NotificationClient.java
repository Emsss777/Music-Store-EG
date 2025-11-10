package app.notification.client;

import app.notification.client.dto.Notification;
import app.notification.client.dto.NotificationPreference;
import app.notification.client.dto.NotificationRequest;
import app.notification.client.dto.UpsertNotificationPreference;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static app.util.UrlPaths.*;

@FeignClient(name = "notification-svc", url = "${notification-svc.base-url}")
public interface NotificationClient {

    @PostMapping(URL_PREFERENCES)
    ResponseEntity<Void> upsertNotificationPreference(
            @RequestBody UpsertNotificationPreference notificationPreference);

    @GetMapping(URL_PREFERENCES)
    ResponseEntity<NotificationPreference> getUserPreference(@RequestParam(name = "userId") UUID userId);

    @GetMapping(URL_PLAIN)
    ResponseEntity<List<Notification>> getNotificationHistory(@RequestParam(name = "userId") UUID userId);

    @PostMapping
    ResponseEntity<Void> sendNotification(@RequestBody NotificationRequest notificationRequest);

    @PutMapping(URL_PREFERENCES + URL_UPDATE_STATUS)
    ResponseEntity<Void> updateNotificationPreference(@RequestParam("userId") UUID userId,
                                                      @RequestParam("enabled") boolean enabled);

    @DeleteMapping
    ResponseEntity<Void> clearHistory(@RequestParam(name = "userId") UUID userId);

    @DeleteMapping(URL_CLEAR)
    ResponseEntity<Void> clearAllHistory();

    @PutMapping
    ResponseEntity<Void> retryFailedNotifications(@RequestParam(name = "userId") UUID userId);
}
