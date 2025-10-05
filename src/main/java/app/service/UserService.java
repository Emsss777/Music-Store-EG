package app.service;

import app.model.dto.RegisterDTO;
import app.model.dto.UserEditDTO;
import app.model.entity.UserEntity;

import java.util.UUID;

public interface UserService {

    void initAuth();

    void registerUser(RegisterDTO registerDTO);

    UserEntity getUserById(UUID userId);

    void editUserDetails(UUID userId, UserEditDTO userEditDTO);
}
