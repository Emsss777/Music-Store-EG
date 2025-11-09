package app.event.payload;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdatedEvent {

    private UUID userId;
    private String email;
    private LocalDateTime updatedOn;
}
