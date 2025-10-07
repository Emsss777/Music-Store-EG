package app.service;

import app.model.dto.RegisterDTO;
import app.model.dto.UserEditDTO;
import app.model.entity.UserEntity;

import java.util.UUID;

public interface UserService {

    void registerUser(RegisterDTO registerDTO);

    UserEntity getUserById(UUID userId);

    UserEntity getUserByUsername(String username);

    void editUserDetails(UUID userId, UserEditDTO userEditDTO);

    void saveUser(UserEntity userEntity);
}
