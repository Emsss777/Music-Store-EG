package app.service.impl;

import app.event.UserRegisteredEventProducer;
import app.event.UserUpdatedEventProducer;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static app.TestDataFactory.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private NotificationService notificationService;

    @Mock
    private UserRegisteredEventProducer userRegisteredEventProducer;

    @Mock
    private UserUpdatedEventProducer userUpdatedEventProducer;

    @InjectMocks
    private UserServiceImpl userService;

    private RegisterDTO registerDTO;

    @BeforeEach
    void setUp() {

        registerDTO = RegisterDTO.builder()
                .username("qa-user")
                .password("password")
                .confirmPassword("password")
                .country(Country.BULGARIA)
                .build();
    }

    @Test
    void registerUser_whenUsernameAlreadyExists_shouldThrowException() {

        when(userRepo.findByUsername(registerDTO.getUsername())).thenReturn(Optional.of(new User()));

        assertThrows(UsernameAlreadyExistException.class, () -> userService.registerUser(registerDTO));
        verify(userRepo, never()).save(any());
    }

    @Test
    void registerUser_whenPasswordsDoNotMatch_shouldThrowException() {

        RegisterDTO invalid = RegisterDTO.builder()
                .username("qa-user")
                .password("password")
                .confirmPassword("different")
                .country(Country.BULGARIA)
                .build();

        when(userRepo.findByUsername(invalid.getUsername())).thenReturn(Optional.empty());

        assertThrows(PasswordMismatchException.class, () -> userService.registerUser(invalid));
        verify(userRepo, never()).save(any());
    }

    @Test
    void registerUser_happyPath_shouldPersistUserAndPublishEvent() {

        when(userRepo.findByUsername(registerDTO.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("hashed");
        when(userRepo.save(any(User.class))).thenAnswer(invocation -> {
            User saved = invocation.getArgument(0, User.class);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        userService.registerUser(registerDTO);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getUsername()).isEqualTo(registerDTO.getUsername());
        assertThat(savedUser.getPassword()).isEqualTo("hashed");
        verify(userRegisteredEventProducer).sendEvent(any());
    }

    @Test
    void getAllUsers_shouldReturnUsersFromRepo() {

        User u1 = aUser();
        User u2 = aUser();
        when(userRepo.findAll()).thenReturn(List.of(u1, u2));

        List<User> result = userService.getAllUsers();

        assertThat(result).containsExactly(u1, u2);
        verify(userRepo).findAll();
    }

    @Test
    void getUserById_whenPresent_shouldReturnUser() {

        UUID userId = UUID.randomUUID();
        User user = aUser();
        user.setId(userId);
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.getUserById(userId);
        assertThat(result).isEqualTo(user);
    }

    @Test
    void getUserById_whenMissing_shouldThrowDomainException() {

        UUID userId = UUID.randomUUID();
        when(userRepo.findById(userId)).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> userService.getUserById(userId));
    }

    @Test
    void getUserByUsername_whenExisting_shouldNotThrow() {

        String username = "qa-user";
        when(userRepo.findByUsername(username)).thenReturn(Optional.of(aUser()));

        userService.getUserByUsername(username);
        verify(userRepo).findByUsername(username);
    }

    @Test
    void getUserByUsername_whenMissing_shouldThrowDomainException() {

        String username = "missing";
        when(userRepo.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> userService.getUserByUsername(username));
    }

    @Test
    void editUserDetails_withEmail_shouldUpdateNotificationPreferenceAndPublishEvent() {

        UUID userId = UUID.randomUUID();
        User user = aUser();
        user.setId(userId);
        user.setCreatedOn(LocalDateTime.now());
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(userRepo.save(user)).thenReturn(user);

        UserEditDTO editDTO = UserEditDTO.builder()
                .firstName("Test")
                .lastName("User")
                .username("qa-user")
                .email("qa@example.com")
                .bio("QA bio")
                .profilePicture("https://example.com/pic.png")
                .country(Country.BULGARIA.name())
                .build();

        userService.editUserDetails(userId, editDTO);

        assertThat(user.getEmail()).isEqualTo(editDTO.getEmail());
        verify(notificationService).saveNotificationPreference(userId, true, editDTO.getEmail());
        verify(userUpdatedEventProducer).sendEvent(any());
        verify(userRepo).save(user);
    }

    @Test
    void editUserDetails_withoutEmail_shouldDisableNotifications() {

        UUID userId = UUID.randomUUID();
        User user = aUser();
        user.setId(userId);
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(userRepo.save(user)).thenReturn(user);

        UserEditDTO editDTO = UserEditDTO.builder()
                .firstName("Test")
                .lastName("User")
                .username("qa-user")
                .email("")
                .bio("QA bio")
                .profilePicture("https://example.com/pic.png")
                .country(Country.BULGARIA.name())
                .build();

        userService.editUserDetails(userId, editDTO);

        verify(notificationService).saveNotificationPreference(userId, false, null);
        verify(userRepo).save(user);
    }

    @Test
    void saveUser_shouldDelegateToRepo() {

        User user = aUser();
        userService.saveUser(user);
        verify(userRepo).save(user);
    }

    @Test
    void changeRole_whenCurrentRoleIsUser_shouldAssignAdmin() {

        UUID userId = UUID.randomUUID();
        User user = aUser();
        user.setId(userId);
        user.setRole(UserRole.USER);
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));

        userService.changeRole(userId);

        assertThat(user.getRole()).isEqualTo(UserRole.ADMIN);
        verify(userRepo).save(user);
    }

    @Test
    void changeRole_whenCurrentRoleIsAdmin_shouldAssignUser() {

        UUID userId = UUID.randomUUID();
        User user = aUser();
        user.setId(userId);
        user.setRole(UserRole.ADMIN);
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));

        userService.changeRole(userId);

        assertThat(user.getRole()).isEqualTo(UserRole.USER);
        verify(userRepo).save(user);
    }

    @Test
    void changeStatus_shouldToggleAndSave() {

        UUID userId = UUID.randomUUID();
        User user = aUser();
        user.setId(userId);
        user.setActive(true);
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));

        userService.changeStatus(userId);

        assertThat(user.isActive()).isFalse();
        verify(userRepo).save(user);
    }

    @Test
    void loadUserByUsername_whenUserMissing_shouldThrowException() {

        when(userRepo.findByUsername("missing")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("missing"));
    }

    @Test
    void loadUserByUsername_whenUserExists_shouldReturnAuthenticationMetadata() {

        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .username("qa-user")
                .password("hashed")
                .country(Country.BULGARIA)
                .role(UserRole.ADMIN)
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
        when(userRepo.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        AuthenticationMetadata result = (AuthenticationMetadata) userService.loadUserByUsername(user.getUsername());

        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getUsername()).isEqualTo(user.getUsername());
        assertThat(result.getAuthorities()).hasSize(1);
        assertThat(result.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_ADMIN");
    }
}