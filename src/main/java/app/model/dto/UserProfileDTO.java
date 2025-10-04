package app.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import static app.util.ExceptionMessages.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {

    String id;

    @Size(max = 20, message = FIRST_NAME_INVALID_LENGTH)
    String firstName;

    @Size(max = 20, message = LAST_NAME_INVALID_LENGTH)
    String lastName;

    @Size(min = 3, max = 20, message = USERNAME_INVALID_LENGTH)
    String username;

    @Email(message = INVALID_EMAIL_FORMAT)
    String email;

    @Size(max = 500, message = BIO_INVALID_LENGTH)
    String bio;

    @URL(message = INVALID_URL_FORMAT)
    String profilePicture;

    private String country;
}
