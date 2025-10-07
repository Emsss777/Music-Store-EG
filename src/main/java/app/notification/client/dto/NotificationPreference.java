package app.notification.client.dto;

import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationPreference {

    private UUID userId;
    private String type;
    private boolean enabled;
    private String contactInfo;

    public static NotificationPreference defaultFor(UUID userId) {

        return NotificationPreference.builder()
                .userId(userId)
                .type("UNKNOWN")
                .enabled(false)
                .contactInfo("Service Unavailable!")
                .build();
    }
}
