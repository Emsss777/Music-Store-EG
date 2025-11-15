package app.notification.client;

import app.notification.client.dto.NotificationDTO;
import app.notification.client.dto.NotificationPreferenceDTO;
import app.notification.client.dto.NotificationRequestDTO;
import app.notification.client.dto.UpsertNotificationDTO;
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
            @RequestBody UpsertNotificationDTO upsertNotificationDTO);

    @GetMapping(URL_PREFERENCES)
    ResponseEntity<NotificationPreferenceDTO> getUserPreference(@RequestParam(name = "userId") UUID userId);

    @GetMapping(URL_PLAIN)
    ResponseEntity<List<NotificationDTO>> getNotificationHistory(@RequestParam(name = "userId") UUID userId);

    @PostMapping
    ResponseEntity<Void> sendNotification(@RequestBody NotificationRequestDTO notificationRequestDTO);

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
