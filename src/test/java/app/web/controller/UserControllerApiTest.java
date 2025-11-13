package app.web.controller;

import app.config.WebMvcConfig;
import app.model.entity.User;
import app.security.AuthenticationMetadata;
import app.service.CartService;
import app.service.OrderService;
import app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;
import java.util.UUID;

import static app.TestDataFactory.aUser;
import static app.util.ModelAttributes.*;
import static app.util.UrlParams.PARAM_ID;
import static app.util.UrlPaths.URL_PROFILE;
import static app.util.UrlPaths.URL_USERS;
import static app.util.Views.VIEW_EDIT_PROFILE;
import static app.util.Views.VIEW_PROFILE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(WebMvcConfig.class)
@WebMvcTest(UserController.class)
class UserControllerApiTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private CartService cartService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        given(cartService.getCartItemCount(any())).willReturn(0);
    }

    @Test
    void getProfilePage_shouldReturnProfileViewWithUserData() throws Exception {

        UUID userId = UUID.randomUUID();
        User user = aUser();
        user.setId(userId);
        int totalAlbumsPurchased = 5;
        BigDecimal totalAmountSpent = new BigDecimal("99.95");

        given(userService.getUserById(userId)).willReturn(user);
        given(orderService.getTotalAlbumsPurchasedByUser(user)).willReturn(totalAlbumsPurchased);
        given(orderService.getTotalAmountSpentByUser(user)).willReturn(totalAmountSpent);

        MockHttpServletRequestBuilder request = get(URL_USERS + URL_PROFILE)
                .with(user(authenticatedUser(userId)));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_PROFILE))
                .andExpect(model().attribute(MODEL_PAGE, VIEW_PROFILE))
                .andExpect(model().attributeExists(MODEL_USER))
                .andExpect(model().attribute(MODEL_TOTAL_ALBUM_PURCHASED, totalAlbumsPurchased))
                .andExpect(model().attribute(MODEL_TOTAL_AMOUNT_SPENT, totalAmountSpent));
    }

    @Test
    void showEditProfile_shouldReturnEditProfileViewWithUserData() throws Exception {

        UUID userId = UUID.randomUUID();
        User user = aUser();
        user.setId(userId);

        given(userService.getUserById(userId)).willReturn(user);

        MockHttpServletRequestBuilder request = get(URL_USERS + PARAM_ID + URL_PROFILE, userId)
                .with(user(authenticatedUser()));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_EDIT_PROFILE))
                .andExpect(model().attributeExists(MODEL_USER))
                .andExpect(model().attributeExists(MODEL_USER_EDIT_DTO));
    }

    @Test
    void updateUserProfile_withValidData_shouldUpdateUserAndRedirectToProfile() throws Exception {

        UUID userId = UUID.randomUUID();
        User user = aUser();
        user.setId(userId);

        given(userService.getUserById(userId)).willReturn(user);

        MockHttpServletRequestBuilder request = put(URL_USERS + PARAM_ID + URL_PROFILE, userId)
                .with(user(authenticatedUser()))
                .with(csrf())
                .param("firstName", "Updated")
                .param("lastName", "Name")
                .param("username", "updatedUser")
                .param("email", "updated@example.com");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/profile"));

        verify(userService).editUserDetails(eq(userId), any());
    }

    @Test
    void updateUserProfile_withInvalidUsername_shouldReturnEditProfileViewWithoutUpdating() throws Exception {

        UUID userId = UUID.randomUUID();
        User user = aUser();
        user.setId(userId);

        given(userService.getUserById(userId)).willReturn(user);

        MockHttpServletRequestBuilder request = put(URL_USERS + PARAM_ID + URL_PROFILE, userId)
                .with(user(authenticatedUser()))
                .with(csrf())
                .param("firstName", "Updated")
                .param("lastName", "Name")
                .param("username", "ab")
                .param("email", "updated@example.com");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_EDIT_PROFILE))
                .andExpect(model().attributeExists(MODEL_USER))
                .andExpect(model().attributeExists(MODEL_USER_EDIT_DTO));

        verify(userService, never()).editUserDetails(eq(userId), any());
    }

    @Test
    void updateUserProfile_withInvalidEmail_shouldReturnEditProfileViewWithoutUpdating() throws Exception {

        UUID userId = UUID.randomUUID();
        User user = aUser();
        user.setId(userId);

        given(userService.getUserById(userId)).willReturn(user);

        MockHttpServletRequestBuilder request = put(URL_USERS + PARAM_ID + URL_PROFILE, userId)
                .with(user(authenticatedUser()))
                .with(csrf())
                .param("firstName", "Updated")
                .param("lastName", "Name")
                .param("username", "updatedUser")
                .param("email", "invalid-email");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_EDIT_PROFILE))
                .andExpect(model().attributeExists(MODEL_USER))
                .andExpect(model().attributeExists(MODEL_USER_EDIT_DTO));

        verify(userService, never()).editUserDetails(eq(userId), any());
    }

    @Test
    void updateUserProfile_withTooLongFirstName_shouldReturnEditProfileViewWithoutUpdating() throws Exception {

        UUID userId = UUID.randomUUID();
        User user = aUser();
        user.setId(userId);

        given(userService.getUserById(userId)).willReturn(user);

        String tooLongFirstName = "A".repeat(21);

        MockHttpServletRequestBuilder request = put(URL_USERS + PARAM_ID + URL_PROFILE, userId)
                .with(user(authenticatedUser()))
                .with(csrf())
                .param("firstName", tooLongFirstName)
                .param("lastName", "Name")
                .param("username", "updatedUser")
                .param("email", "updated@example.com");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_EDIT_PROFILE))
                .andExpect(model().attributeExists(MODEL_USER))
                .andExpect(model().attributeExists(MODEL_USER_EDIT_DTO));

        verify(userService, never()).editUserDetails(eq(userId), any());
    }

    private AuthenticationMetadata authenticatedUser() {

        return authenticatedUser(UUID.randomUUID());
    }

    private AuthenticationMetadata authenticatedUser(UUID userId) {

        return AuthenticationMetadata.builder()
                .userId(userId)
                .username("qa-user")
                .password("password")
                .role(app.model.enums.UserRole.USER)
                .isActive(true)
                .build();
    }
}
