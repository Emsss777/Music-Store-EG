package app.mapper;

import app.model.dto.RegisterDTO;
import app.model.dto.UserBasicDTO;
import app.model.dto.UserEditDTO;
import app.model.dto.UserListDTO;
import app.model.entity.User;
import app.model.enums.Country;
import app.model.enums.UserRole;
import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class UserMapper {

    public static User fromRegisterDTO(RegisterDTO register, PasswordEncoder encoder) {

        if (register == null) return null;

        return User.builder()
                .username(register.getUsername())
                .password(encoder.encode(register.getPassword()))
                .country(register.getCountry())
                .role(UserRole.USER)
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
    }

    public static void updateUserFromEditDTO(User user, UserEditDTO userEdit) {

        user.setFirstName(userEdit.getFirstName())
                .setLastName(userEdit.getLastName())
                .setUsername(userEdit.getUsername())
                .setEmail(userEdit.getEmail())
                .setBio(userEdit.getBio())
                .setProfilePicture(userEdit.getProfilePicture())
                .setCountry(Country.valueOf(userEdit.getCountry()))
                .setUpdatedOn(LocalDateTime.now());
    }

    public static UserBasicDTO toBasicDTO(User user) {

        if (user == null) return null;

        return UserBasicDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .country(user.getCountry())
                .role(user.getRole())
                .isActive(user.isActive())
                .createdOn(user.getCreatedOn())
                .build();
    }

    public static UserListDTO toListDTO(User user) {

        if (user == null) return null;

        return UserListDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .country(user.getCountry())
                .active(user.isActive())
                .createdOn(user.getCreatedOn())
                .build();
    }

    public static List<UserListDTO> toListDTOList(List<User> users) {

        if (users == null) return Collections.emptyList();

        return users.stream()
                .map(UserMapper::toListDTO)
                .toList();
    }
}
