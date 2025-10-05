package app.mapper;

import app.model.dto.UserEditDTO;
import app.model.entity.UserEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserEditMapper {

    public static UserEditDTO mapUserToUserEditDTO(UserEntity user) {

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
