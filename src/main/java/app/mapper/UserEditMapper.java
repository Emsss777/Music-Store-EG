package app.mapper;

import app.model.dto.UserEditDTO;
import app.model.entity.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserEditMapper {

    public static UserEditDTO mapUserToUserEditDTO(User user) {

        if (user == null) return null;

        return UserEditDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .profilePicture(user.getProfilePicture())
                .country(String.valueOf(user.getCountry()))
                .build();
    }
}
