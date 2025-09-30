package app.model.dto;

import app.model.enums.Country;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {

    @NotNull(message = "Username cannot be empty!")
    @Size(min = 3, max = 20, message = "Username length must be between 3 and 20 characters!")
    private String username;

    @NotNull(message = "Email cannot be empty!")
    @Email
    private String email;

    @NotNull(message = "Password cannot be empty!")
    @Size(min = 3, max = 20, message = "Password length must be between 3 and 20 characters!")
    private String password;

    @NotNull(message = "Password cannot be empty!")
    @Size(min = 3, max = 20, message = "Password length must be between 3 and 20 characters!")
    private String confirmPassword;

    @NotNull(message = "You must select a country!")
    private Country country;
}
