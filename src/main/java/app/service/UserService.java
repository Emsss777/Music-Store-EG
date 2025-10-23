package app.service;

import app.model.dto.RegisterDTO;
import app.model.dto.UserEditDTO;
import app.model.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    void registerUser(RegisterDTO registerDTO);

    User getUserById(UUID userId);

    User getUserByUsername(String username);

    void editUserDetails(UUID userId, UserEditDTO userEditDTO);

    void saveUser(User user);

    List<User> getAllUsers();

    void changeStatus(UUID id);

    void changeRole(UUID id);
}
