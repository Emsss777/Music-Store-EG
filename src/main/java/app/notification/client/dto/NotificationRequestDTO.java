package app.notification.client.dto;

import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDTO {

    private UUID userId;
    private String subject;
    private String body;

    public static NotificationRequestDTO defaultFor(UUID userId, String subject, String body) {

        return NotificationRequestDTO.builder()
                .userId(userId)
                .subject(subject)
                .body(body)
                .build();
    }
}
