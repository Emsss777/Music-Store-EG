package app.service.impl;

import app.aspect.VeryImportant;
import app.event.UserRegisteredEventProducer;
import app.event.payload.UserRegisteredEvent;
import app.exception.DomainException;
import app.exception.PasswordMismatchException;
import app.exception.UsernameAlreadyExistException;
import app.model.dto.RegisterDTO;
import app.model.dto.UserEditDTO;
import app.model.entity.User;
import app.model.enums.Country;
import app.model.enums.UserRole;
import app.notification.services.NotificationService;
import app.repository.UserRepo;
import app.security.AuthenticationMetadata;
import app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static app.util.ExceptionMessages.*;
import static app.util.SuccessMessages.USER_CREATED;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;
    private final UserRegisteredEventProducer userRegisteredEventProducer;

    @Override
    @Cacheable("users")
    public List<User> getAllUsers() {

        return userRepo.findAll();
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public void registerUser(RegisterDTO registerDTO) {

        Optional<User> existingUser = userRepo.findByUsername(registerDTO.getUsername());

        if (existingUser.isPresent()) {
            throw new UsernameAlreadyExistException(
                    USERNAME_ALREADY_EXISTS.formatted(registerDTO.getUsername()));
        }

        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new PasswordMismatchException(PASSWORD_DO_NOT_MATCH);
        }

        User user = userRepo.save(initializeUser(registerDTO));

        UserRegisteredEvent event = UserRegisteredEvent.builder()
                .userId(user.getId())
                .createdOn(user.getCreatedOn())
                .build();
        userRegisteredEventProducer.sendEvent(event);

        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_GREEN, USER_CREATED), user.getUsername(), user.getId());

    }

    private User initializeUser(RegisterDTO registerDTO) {

        return User.builder()
                .username(registerDTO.getUsername())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .country(registerDTO.getCountry())
                .role(UserRole.USER)
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
    }

    @Override
    @VeryImportant
    public User getUserById(UUID userId) {

        return userRepo.findById(userId).orElseThrow(() ->
                new DomainException(USER_DOES_NOT_EXIST.formatted(userId)));
    }

    @Override
    public User getUserByUsername(String username) {

        return userRepo.findByUsername(username).orElseThrow(() ->
                new DomainException(USER_DOES_NOT_EXIST.formatted(username)));
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public void editUserDetails(UUID userId, UserEditDTO userEditDTO) {

        User user = getUserById(userId);

        user.setFirstName(userEditDTO.getFirstName());
        user.setLastName(userEditDTO.getLastName());
        user.setUsername(userEditDTO.getUsername());
        user.setEmail(userEditDTO.getEmail());
        user.setBio(userEditDTO.getBio());
        user.setProfilePicture(userEditDTO.getProfilePicture());
        user.setCountry(Country.valueOf(userEditDTO.getCountry()));

        if (!userEditDTO.getEmail().isBlank()) {
            notificationService.saveNotificationPreference(userId, true, userEditDTO.getEmail());
        } else {
            notificationService.saveNotificationPreference(userId, false, null);
        }

        userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn(USERNAME_DOES_NOT_EXIST, username);
                    return new UsernameNotFoundException(AnsiOutput.toString(AnsiColor.BRIGHT_MAGENTA, BAD_CREDENTIALS));
                });

        return AuthenticationMetadata.builder()
                .username(user.getUsername())
                .userId(user.getId())
                .password(user.getPassword())
                .role(user.getRole())
                .isActive(user.isActive())
                .build();
    }

    @Override
    public void saveUser(User user) {

        userRepo.save(user);
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public void changeStatus(UUID id) {

        User user = getUserById(id);
        user.setActive(!user.isActive());
        userRepo.save(user);
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public void changeRole(UUID id) {

        User user = getUserById(id);

        if (user.getRole() == UserRole.USER) {
            user.setRole(UserRole.ADMIN);
        } else {
            user.setRole(UserRole.USER);
        }

        userRepo.save(user);
    }
}
