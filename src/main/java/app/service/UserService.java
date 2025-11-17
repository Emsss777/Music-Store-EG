package app.service;

import app.model.dto.RegisterDTO;
import app.model.dto.UserEditDTO;
import app.model.dto.UserListDTO;
import app.model.dto.UserProfileDTO;
import app.model.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    void registerUser(RegisterDTO registerDTO);

    User getUserById(UUID userId);

    UserProfileDTO getUserProfileDTO(UUID userId);

    UserEditDTO getUserEditDTO(UUID userId);

    void getUserByUsername(String username);

    void editUserDetails(UUID userId, UserEditDTO userEditDTO);

    void saveUser(User user);

    List<User> getAllUsers();

    List<UserListDTO> getAllUsersDTO();

    void changeStatus(UUID id);

    void changeRole(UUID id);
}
