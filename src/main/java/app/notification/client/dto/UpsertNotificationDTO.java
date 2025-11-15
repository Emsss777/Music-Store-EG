package app.notification.client.dto;

import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpsertNotificationDTO {

    private UUID userId;
    private String type;
    private boolean enabled;
    private String contactInfo;

    public static UpsertNotificationDTO toUpsert(UUID userId, boolean isEmailEnabled, String email) {

        return UpsertNotificationDTO.builder()
                .userId(userId)
                .type("EMAIL")
                .enabled(isEmailEnabled)
                .contactInfo(email)
                .build();
    }
}
