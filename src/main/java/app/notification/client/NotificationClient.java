package app.notification.client;

import app.notification.client.dto.UpsertNotificationPreference;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static app.util.FeignClients.*;
import static app.util.UrlPaths.URL_PREFERENCES;

@FeignClient(name = NOTIFICATION_SVC, url = NOTIFICATION_SVC_URL)
public interface NotificationClient {

    @PostMapping(URL_PREFERENCES)
    ResponseEntity<Void> upsertNotificationPreference(
            @RequestBody UpsertNotificationPreference notificationPreference);
}
