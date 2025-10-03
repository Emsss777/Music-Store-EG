package app.model.dto;

import app.model.enums.Country;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import static app.util.ExceptionMessages.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {

    @NotNull(message = USER_CANNOT_BE_EMPTY)
    @Size(min = 3, max = 20, message = USERNAME_INVALID_LENGTH)
    private String username;

    @NotNull(message = PASSWORD_CANNOT_BE_EMPTY)
    @Size(min = 3, max = 20, message = PASSWORD_INVALID_LENGTH)
    private String password;

    @NotNull(message = PASSWORD_CANNOT_BE_EMPTY)
    @Size(min = 3, max = 20, message = PASSWORD_INVALID_LENGTH)
    private String confirmPassword;

    @NotNull(message = SELECT_A_COUNTRY)
    private Country country;
}
