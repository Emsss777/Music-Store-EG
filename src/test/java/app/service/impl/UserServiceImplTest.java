package app.service.impl;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
public class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private NotificationService notificationService;

    @Mock
    private UserRegisteredEventProducer userRegisteredEventProducer;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UUID userId;
    private String username;

    @BeforeEach
    void setUp() {

        userId = UUID.randomUUID();
        username = "testUser";

        user = User.builder()
                .id(userId)
                .username(username)
                .password("encodedPassword")
                .country(Country.BULGARIA)
                .role(UserRole.USER)
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should throw UsernameAlreadyExistException when username already exists")
    void registerUser_WhenUsernameExists_ShouldThrowException() {

        // Given
        RegisterDTO registerDTO = RegisterDTO.builder()
                .username(username)
                .password("password123")
                .confirmPassword("password123")
                .country(Country.BULGARIA)
                .build();

        when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));

        // When & Then
        UsernameAlreadyExistException exception =
                assertThrows(UsernameAlreadyExistException.class, () -> userService.registerUser(registerDTO));

        assertTrue(exception.getMessage().contains(username));
        verify(userRepo, never()).save(any(User.class));
        verify(userRegisteredEventProducer, never()).sendEvent(any(UserRegisteredEvent.class));
        verify(notificationService, never()).saveNotificationPreference(any(), anyBoolean(), any());
    }

    @Test
    @DisplayName("Should throw PasswordMismatchException when passwords do not match")
    void registerUser_WhenPasswordsDoNotMatch_ShouldThrowException() {

        // Given
        RegisterDTO registerDTO = RegisterDTO.builder()
                .username(username)
                .password("password123")
                .confirmPassword("differentPassword")
                .country(Country.BULGARIA)
                .build();

        when(userRepo.findByUsername(username)).thenReturn(Optional.empty());

        // When & Then
        PasswordMismatchException exception =
                assertThrows(PasswordMismatchException.class, () -> userService.registerUser(registerDTO));

        assertNotNull(exception.getMessage());
        verify(userRepo, never()).save(any(User.class));
        verify(userRegisteredEventProducer, never()).sendEvent(any(UserRegisteredEvent.class));
        verify(notificationService, never()).saveNotificationPreference(any(), anyBoolean(), any());
    }

    @Test
    @DisplayName("Should successfully register user when all conditions are met")
    void registerUser_WhenValidData_ShouldRegisterUserSuccessfully() {

        // Given
        RegisterDTO registerDTO = RegisterDTO.builder()
                .username(username)
                .password("password123")
                .confirmPassword("password123")
                .country(Country.BULGARIA)
                .build();

        String encodedPassword = "encodedPassword123";
        User savedUser = User.builder()
                .id(userId)
                .username(username)
                .password(encodedPassword)
                .country(Country.BULGARIA)
                .role(UserRole.USER)
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        when(userRepo.findByUsername(username)).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn(encodedPassword);
        when(userRepo.save(any(User.class))).thenReturn(savedUser);

        // When
        userService.registerUser(registerDTO);

        // Then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertEquals(username, capturedUser.getUsername());
        assertEquals(encodedPassword, capturedUser.getPassword());
        assertEquals(Country.BULGARIA, capturedUser.getCountry());
        assertEquals(UserRole.USER, capturedUser.getRole());
        assertTrue(capturedUser.isActive());
        assertNotNull(capturedUser.getCreatedOn());
        assertNotNull(capturedUser.getUpdatedOn());

        ArgumentCaptor<UserRegisteredEvent> eventCaptor = ArgumentCaptor.forClass(UserRegisteredEvent.class);
        verify(userRegisteredEventProducer).sendEvent(eventCaptor.capture());

        UserRegisteredEvent capturedEvent = eventCaptor.getValue();
        assertEquals(userId, capturedEvent.getUserId());
        assertNotNull(capturedEvent.getCreatedOn());
    }

    @Test
    @DisplayName("Should return user when user exists with given ID")
    void getUserById_WhenUserExists_ShouldReturnUser() {

        // Given
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));

        // When
        User result = userService.getUserById(userId);

        // Then
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(username, result.getUsername());
        verify(userRepo).findById(userId);
    }

    @Test
    @DisplayName("Should throw DomainException when user does not exist with given ID")
    void getUserById_WhenUserDoesNotExist_ShouldThrowException() {

        // Given
        UUID nonExistentUserId = UUID.randomUUID();
        when(userRepo.findById(nonExistentUserId)).thenReturn(Optional.empty());

        // When & Then
        DomainException exception =
                assertThrows(DomainException.class, () -> userService.getUserById(nonExistentUserId));

        assertTrue(exception.getMessage().contains(nonExistentUserId.toString()));
        verify(userRepo).findById(nonExistentUserId);
    }

    @Test
    @DisplayName("Should return user when user exists with given username")
    void getUserByUsername_WhenUserExists_ShouldReturnUser() {

        // Given
        when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));

        // When
        User result = userService.getUserByUsername(username);

        // Then
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(username, result.getUsername());
        verify(userRepo).findByUsername(username);
    }

    @Test
    @DisplayName("Should throw DomainException when user does not exist with given username")
    void getUserByUsername_WhenUserDoesNotExist_ShouldThrowException() {

        // Given
        String nonExistentUsername = "nonexistent";
        when(userRepo.findByUsername(nonExistentUsername)).thenReturn(Optional.empty());

        // When & Then
        DomainException exception =
                assertThrows(DomainException.class, () -> userService.getUserByUsername(nonExistentUsername));

        assertTrue(exception.getMessage().contains(nonExistentUsername));
        verify(userRepo).findByUsername(nonExistentUsername);
    }

    @Test
    @DisplayName("Should successfully edit user details with email")
    void editUserDetails_WhenValidDataWithEmail_ShouldEditUserSuccessfully() {

        // Given
        UserEditDTO userEditDTO = UserEditDTO.builder()
                .firstName("Emil")
                .lastName("Ganchev")
                .username("Emil777")
                .email("ems@softuni.bg")
                .bio("Test Bio")
                .profilePicture("https://example.com/pic.jpg")
                .country("SPAIN")
                .build();

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(userRepo.save(any(User.class))).thenReturn(user);

        // When
        userService.editUserDetails(userId, userEditDTO);

        // Then
        assertEquals("Emil", user.getFirstName());
        assertEquals("Ganchev", user.getLastName());
        assertEquals("Emil777", user.getUsername());
        assertEquals("ems@softuni.bg", user.getEmail());
        assertEquals("Test Bio", user.getBio());
        assertEquals("https://example.com/pic.jpg", user.getProfilePicture());
        assertEquals(Country.SPAIN, user.getCountry());

        verify(userRepo).save(user);
        verify(notificationService).saveNotificationPreference(userId, true, "ems@softuni.bg");
    }

    @Test
    @DisplayName("Should successfully edit user details without email")
    void editUserDetails_WhenValidDataWithoutEmail_ShouldEditUserSuccessfully() {

        // Given
        UserEditDTO userEditDTO = UserEditDTO.builder()
                .firstName("Emil")
                .lastName("Ganchev")
                .username("Emil777")
                .email("")
                .bio("Test Bio")
                .profilePicture("https://example.com/pic.jpg")
                .country("GERMANY")
                .build();

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(userRepo.save(any(User.class))).thenReturn(user);

        // When
        userService.editUserDetails(userId, userEditDTO);

        // Then
        assertEquals("Emil", user.getFirstName());
        assertEquals("Ganchev", user.getLastName());
        assertEquals("Emil777", user.getUsername());
        assertEquals("", user.getEmail());
        assertEquals("Test Bio", user.getBio());
        assertEquals("https://example.com/pic.jpg", user.getProfilePicture());
        assertEquals(Country.GERMANY, user.getCountry());

        verify(userRepo).save(user);
        verify(notificationService).saveNotificationPreference(userId, false, null);
    }

    @Test
    @DisplayName("Should throw DomainException when trying to edit non-existent user")
    void editUserDetails_WhenUserDoesNotExist_ShouldThrowException() {

        // Given
        UUID nonExistentUserId = UUID.randomUUID();
        UserEditDTO userEditDTO = UserEditDTO.builder()
                .firstName("Emil")
                .lastName("Ganchev")
                .username("Emil777")
                .email("ems@softuni.bg")
                .country("SPAIN")
                .build();

        when(userRepo.findById(nonExistentUserId)).thenReturn(Optional.empty());

        // When & Then
        DomainException exception =
                assertThrows(DomainException.class, () -> userService.editUserDetails(nonExistentUserId, userEditDTO));

        assertTrue(exception.getMessage().contains(nonExistentUserId.toString()));
        verify(userRepo, never()).save(any(User.class));
        verify(notificationService, never()).saveNotificationPreference(any(), anyBoolean(), any());
    }

    @Test
    @DisplayName("Should return UserDetails when user exists")
    void loadUserByUsername_WhenUserExists_ShouldReturnUserDetails() {

        // Given
        when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));

        // When
        UserDetails result = userService.loadUserByUsername(username);

        // Then
        assertNotNull(result);
        assertInstanceOf(AuthenticationMetadata.class, result);

        AuthenticationMetadata authMetadata = (AuthenticationMetadata) result;
        assertEquals(username, authMetadata.getUsername());
        assertEquals(userId, authMetadata.getUserId());
        assertEquals("encodedPassword", authMetadata.getPassword());
        assertEquals(UserRole.USER, authMetadata.getRole());
        assertTrue(authMetadata.isActive());
        assertTrue(authMetadata.isAccountNonExpired());
        assertTrue(authMetadata.isAccountNonLocked());
        assertTrue(authMetadata.isCredentialsNonExpired());
        assertTrue(authMetadata.isEnabled());

        assertEquals(1, authMetadata.getAuthorities().size());
        assertTrue(authMetadata.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER")));

        verify(userRepo).findByUsername(username);
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user does not exist")
    void loadUserByUsername_WhenUserDoesNotExist_ShouldThrowException() {

        // Given
        String nonExistentUsername = "nonexistent";
        when(userRepo.findByUsername(nonExistentUsername)).thenReturn(Optional.empty());

        // When & Then
        UsernameNotFoundException exception =
                assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(nonExistentUsername));

        assertNotNull(exception.getMessage());
        verify(userRepo).findByUsername(nonExistentUsername);
    }

    @Test
    @DisplayName("Should successfully save user")
    void saveUser_WhenValidUser_ShouldSaveUserSuccessfully() {

        // Given
        when(userRepo.save(user)).thenReturn(user);

        // When
        userService.saveUser(user);

        // Then
        verify(userRepo).save(user);
    }
}