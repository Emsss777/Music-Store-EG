package app.service.impl;

import app.aspect.VeryImportant;
import app.event.UserRegisteredEventProducer;
import app.event.UserUpdatedEventProducer;
import app.event.payload.UserRegisteredEvent;
import app.event.payload.UserUpdatedEvent;
import app.exception.DomainException;
import app.exception.PasswordMismatchException;
import app.exception.UsernameAlreadyExistException;
import app.mapper.UserMapper;
import app.model.dto.RegisterDTO;
import app.model.dto.UserEditDTO;
import app.model.dto.UserListDTO;
import app.model.entity.User;
import app.model.enums.UserRole;
import app.notification.services.NotificationService;
import app.repository.UserRepo;
import app.security.AuthenticationMetadata;
import app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;

import java.util.List;
import java.util.UUID;

import static app.util.ExceptionMessages.*;
import static app.util.LogMessages.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;
    private final UserRegisteredEventProducer userRegisteredEventProducer;
    private final UserUpdatedEventProducer userUpdatedEventProducer;

    @Override
    public List<User> getAllUsers() {

        return userRepo.findAll();
    }

    @Override
    public List<UserListDTO> getAllUsersDTO() {

        List<User> users = getAllUsers();
        return UserMapper.toListDTOList(users);
    }

    @Override
    @Transactional
    public void registerUser(RegisterDTO registerDTO) {

        userRepo.findByUsername(registerDTO.getUsername())
                .ifPresent(user -> {
                    throw new UsernameAlreadyExistException(
                            USERNAME_ALREADY_EXISTS.formatted(registerDTO.getUsername()));
                });

        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new PasswordMismatchException(PASSWORD_DO_NOT_MATCH);
        }

        User user = userRepo.save(UserMapper.fromRegisterDTO(registerDTO, passwordEncoder));

        UserRegisteredEvent event = UserRegisteredEvent.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .createdOn(user.getCreatedOn())
                .build();
        userRegisteredEventProducer.sendEvent(event);

        log.info(AnsiOutput.toString(
                AnsiColor.BRIGHT_GREEN, USER_CREATED),user.getUsername(), user.getId());

    }

    @Override
    @VeryImportant
    public User getUserById(UUID userId) {

        return userRepo.findById(userId).orElseThrow(() ->
                new DomainException(USER_DOES_NOT_EXIST.formatted(userId)));
    }

    @Override
    public void getUserByUsername(String username) {

        userRepo.findByUsername(username).orElseThrow(() ->
                new DomainException(USER_DOES_NOT_EXIST.formatted(username)));
    }

    @Override
    public void editUserDetails(UUID userId, UserEditDTO userEditDTO) {

        User user = getUserById(userId);
        UserMapper.updateUserFromEditDTO(user, userEditDTO);

        if (!userEditDTO.getEmail().isBlank()) {
            notificationService.saveNotificationPreference(userId, true, userEditDTO.getEmail());
        } else {
            notificationService.saveNotificationPreference(userId, false, null);
        }

        User saveUser = userRepo.save(user);

        UserUpdatedEvent event = UserUpdatedEvent.builder()
                .userId(saveUser.getId())
                .email(saveUser.getEmail())
                .updatedOn(saveUser.getCreatedOn())
                .build();
        userUpdatedEventProducer.sendEvent(event);

        log.info(AnsiOutput.toString(
                AnsiColor.BRIGHT_GREEN, USER_DETAILS_UPDATED), user.getUsername(), userId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepo.findByUsername(username)
                .map(user -> AuthenticationMetadata.builder()
                        .username(user.getUsername())
                        .userId(user.getId())
                        .password(user.getPassword())
                        .role(user.getRole())
                        .isActive(user.isActive())
                        .build())
                .orElseThrow(() -> {
                    log.warn(AnsiOutput.toString(AnsiColor.BRIGHT_YELLOW, USERNAME_DOES_NOT_EXIST), username);
                    return new UsernameNotFoundException(
                            AnsiOutput.toString(AnsiColor.BRIGHT_MAGENTA, BAD_CREDENTIALS));
                });
    }

    @Override
    public void saveUser(User user) {

        userRepo.save(user);
    }

    @Override
    public void changeStatus(UUID id) {

        User user = getUserById(id);
        boolean oldStatus = user.isActive();
        user.setActive(!user.isActive());
        userRepo.save(user);
        log.info(AnsiOutput.toString(
                AnsiColor.BRIGHT_GREEN, USER_STATUS_CHANGED), user.getUsername(), oldStatus, user.isActive());
    }

    @Override
    public void changeRole(UUID id) {

        User user = getUserById(id);
        UserRole oldRole = user.getRole();

        if (user.getRole() == UserRole.USER) {
            user.setRole(UserRole.ADMIN);
        } else {
            user.setRole(UserRole.USER);
        }

        userRepo.save(user);
        log.info(AnsiOutput.toString(
                AnsiColor.BRIGHT_GREEN, USER_ROLE_CHANGED), user.getUsername(), oldRole, user.getRole());
    }
}
