package app.mapper;

import app.model.dto.UserBasicDTO;
import app.model.dto.UserListDTO;
import app.model.entity.User;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.List;

@UtilityClass
public class UserMapper {

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
