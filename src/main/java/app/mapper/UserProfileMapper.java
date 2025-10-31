package app.mapper;

import app.model.dto.UserProfileDTO;
import app.model.entity.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserProfileMapper {

    public static UserProfileDTO toDTO(User user) {

        if (user == null) return null;

        return UserProfileDTO.builder()
                .id(String.valueOf(user.getId()))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .profilePicture(user.getProfilePicture())
                .country(String.valueOf(user.getCountry()))
                .isActive(user.isActive())
                .birthDate(user.getBirthDate())
                .build();
    }
}
