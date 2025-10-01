package app.service;

import app.model.dto.RegisterDTO;

public interface UserService {

    void initAuth();

    void registerUser(RegisterDTO registerDTO);
}
