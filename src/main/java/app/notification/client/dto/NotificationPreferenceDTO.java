package app.notification.client.dto;

import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationPreferenceDTO {

    private UUID userId;
    private String type;
    private boolean enabled;
    private String contactInfo;

    public static NotificationPreferenceDTO defaultFor(UUID userId) {

        return NotificationPreferenceDTO.builder()
                .userId(userId)
                .type("UNKNOWN")
                .enabled(false)
                .contactInfo("Service Unavailable!")
                .build();
    }
}
