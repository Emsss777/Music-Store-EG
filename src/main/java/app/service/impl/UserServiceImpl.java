package app.service.impl;

import app.exception.UsernameAlreadyExistException;
import app.model.dto.RegisterDTO;
import app.model.entity.UserEntity;
import app.model.enums.UserRole;
import app.repository.UserRepo;
import app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder) {

        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    @Override
    public void registerUser(RegisterDTO registerDTO) {

        Optional<UserEntity> optionalUser = userRepo.findByUsername(registerDTO.getUsername());

        if (optionalUser.isPresent()) {

            throw new UsernameAlreadyExistException(
                    "Username [%s] Already Exist!".formatted(registerDTO.getUsername()));
        }

        UserEntity user = userRepo.save(initializeUser(registerDTO));

        log.info("Successfully created new User account for username [{}] and ID [{}]",
                user.getUsername(), user.getId());
    }

    private UserEntity initializeUser(RegisterDTO registerDTO) {

        return UserEntity.builder()
                .username(registerDTO.getUsername())
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .country(registerDTO.getCountry())
                .role(UserRole.USER)
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
    }
}
