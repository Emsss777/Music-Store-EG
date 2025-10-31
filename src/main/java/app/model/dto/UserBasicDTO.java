package app.model.dto;

import app.model.enums.Country;
import app.model.enums.UserRole;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserBasicDTO {

    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Country country;
    private UserRole role;
    private boolean isActive;
    private LocalDateTime createdOn;
}
