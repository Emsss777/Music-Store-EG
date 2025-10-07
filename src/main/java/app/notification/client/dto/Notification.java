package app.notification.client.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    private String subject;
    private LocalDateTime createdOn;
    private String status;
    private String type;

    public static List<Notification> defaultHistory() {

        Notification fallback = Notification.builder()
                .subject("Service Unavailable!")
                .createdOn(LocalDateTime.now())
                .status("N/A")
                .type("N/A")
                .build();

        return Collections.singletonList(fallback);
    }
}
