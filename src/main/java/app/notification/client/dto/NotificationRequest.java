package app.notification.client.dto;

import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {

    private UUID userId;
    private String subject;
    private String body;

    public static NotificationRequest defaultFor(UUID userId, String subject, String body) {

        return NotificationRequest.builder()
                .userId(userId)
                .subject(subject)
                .body(body)
                .build();
    }
}
