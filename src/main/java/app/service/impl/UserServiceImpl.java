package app.service.impl;

import app.aspect.VeryImportant;
import app.exception.DomainException;
import app.exception.PasswordMismatchException;
import app.exception.UsernameAlreadyExistException;
import app.model.dto.RegisterDTO;
import app.model.entity.UserEntity;
import app.model.enums.Country;
import app.model.enums.UserRole;
import app.repository.UserRepo;
import app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static app.util.ExceptionMessages.*;
import static app.util.SuccessMessages.USER_CREATED;

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

    @Override
    public void initAuth() {

        if (userRepo.count() != 0) {
            return;
        }

        UserEntity testAdmin = new UserEntity()
                .setUsername("admin1")
                .setFirstName("FirstName1")
                .setLastName("LastName1")
                .setEmail("admin1@softuni.bg")
                .setPassword(passwordEncoder.encode("123456"))
                .setBirthDate(LocalDate.parse("1999-09-19"))
                .setCountry(Country.BULGARIA)
                .setRole(UserRole.ADMIN)
                .setActive(true)
                .setCreatedOn(LocalDateTime.now())
                .setUpdatedOn(LocalDateTime.now());

        userRepo.save(testAdmin);

        UserEntity testUser = new UserEntity()
                .setUsername("user1")
                .setFirstName("FirstName2")
                .setLastName("LastName2")
                .setEmail("user1@softuni.bg")
                .setPassword(passwordEncoder.encode("456123"))
                .setBirthDate(LocalDate.parse("1989-08-18"))
                .setCountry(Country.SPAIN)
                .setRole(UserRole.USER)
                .setActive(true)
                .setCreatedOn(LocalDateTime.now())
                .setUpdatedOn(LocalDateTime.now());

        userRepo.save(testUser);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    @Override
    public void registerUser(RegisterDTO registerDTO) {

        Optional<UserEntity> optionalUser = userRepo.findByUsername(registerDTO.getUsername());

        if (optionalUser.isPresent()) {
            throw new UsernameAlreadyExistException(
                    USERNAME_ALREADY_EXIST.formatted(registerDTO.getUsername()));
        }

        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new PasswordMismatchException(PASSWORD_DO_NOT_MATCH);
        }

        UserEntity user = userRepo.save(initializeUser(registerDTO));

        log.info(USER_CREATED, user.getUsername(), user.getId());
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

    @VeryImportant
    @Override
    public UserEntity getUserById(UUID userId) {

        return userRepo.findById(userId).orElseThrow(() ->
                new DomainException(USER_DOES_NOT_EXIST.formatted(userId)));
    }
}
