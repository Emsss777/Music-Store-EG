package app.mapper;

import app.model.dto.UserProfileDTO;
import app.model.entity.User;
import lombok.NoArgsConstructor;

import static app.util.HtmlSafe.esc;

@NoArgsConstructor
public class UserProfileMapper {

    public static UserProfileDTO toSafeDTO(User user) {

        return new UserProfileDTO(
                user.getId() != null ? user.getId().toString() : null,
                esc(user.getFirstName()),
                esc(user.getLastName()),
                esc(user.getUsername()),
                esc(user.getEmail()),
                esc(user.getBio()),
                user.getProfilePicture(),
                esc(String.valueOf(user.getCountry()))
        );
    }
}
