package app.notification.client.dto;

import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpsertNotificationPreference {

    private UUID userId;
    private boolean notificationEnabled;
    private String type;
    private String contactInfo;
}
