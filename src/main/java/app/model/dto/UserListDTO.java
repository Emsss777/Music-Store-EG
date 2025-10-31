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
public class UserListDTO {

    private UUID id;
    private String username;
    private String email;
    private UserRole role;
    private Country country;
    private boolean active;
    private LocalDateTime createdOn;
}
