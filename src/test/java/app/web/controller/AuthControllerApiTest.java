package app.web.controller;

import app.config.WebMvcConfig;
import app.mapper.AlbumMapper;
import app.model.dto.AlbumDTO;
import app.model.entity.Album;
import app.service.AlbumService;
import app.service.CartService;
import app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static app.TestDataFactory.anAlbum;
import static app.TestDataFactory.anArtist;
import static app.util.ExceptionMessages.INCORRECT_USERNAME_OR_PASSWORD;
import static app.util.ModelAttributes.*;
import static app.util.UrlPaths.URL_LOGIN;
import static app.util.UrlPaths.URL_REGISTER;
import static app.util.Views.VIEW_LOGIN;
import static app.util.Views.VIEW_REGISTER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(WebMvcConfig.class)
@WebMvcTest(AuthController.class)
class AuthControllerApiTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AlbumService albumService;

    @MockitoBean
    private CartService cartService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        given(cartService.getCartItemCount(any())).willReturn(0);
    }

    @Test
    void getLoginPage_withoutErrorParam_shouldReturnLoginViewWithAlbums() throws Exception {

        List<Album> randomAlbums = List.of(
                anAlbum(anArtist()),
                anAlbum(anArtist()),
                anAlbum(anArtist())
        );

        List<AlbumDTO> randomAlbumDTOs = AlbumMapper.toDTOList(randomAlbums);
        given(albumService.getRandomAlbumsDTO(6)).willReturn(randomAlbumDTOs);

        MockHttpServletRequestBuilder request = get(URL_LOGIN);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_LOGIN))
                .andExpect(model().attribute(MODEL_LOGIN_DTO, org.hamcrest.Matchers.notNullValue()))
                .andExpect(model().attributeExists(MODEL_ALBUMS))
                .andExpect(model().attribute(MODEL_ALBUMS, org.hamcrest.Matchers.hasSize(3)))
                .andExpect(model().attributeDoesNotExist(MODEL_ERROR_MESSAGE));
    }

    @Test
    void getLoginPage_withErrorParam_shouldReturnLoginViewWithErrorMessage() throws Exception {

        List<Album> randomAlbums = List.of(anAlbum(anArtist()));

        List<AlbumDTO> randomAlbumDTOs = AlbumMapper.toDTOList(randomAlbums);
        given(albumService.getRandomAlbumsDTO(6)).willReturn(randomAlbumDTOs);

        MockHttpServletRequestBuilder request = get(URL_LOGIN)
                .param("error", "true");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_LOGIN))
                .andExpect(model().attribute(MODEL_LOGIN_DTO, org.hamcrest.Matchers.notNullValue()))
                .andExpect(model().attributeExists(MODEL_ALBUMS))
                .andExpect(model().attribute(MODEL_ALBUMS, org.hamcrest.Matchers.hasSize(1)))
                .andExpect(model().attribute(MODEL_ERROR_MESSAGE, INCORRECT_USERNAME_OR_PASSWORD));
    }

    @Test
    void getRegisterPage_shouldReturnRegisterViewWithAlbums() throws Exception {

        List<Album> randomAlbums = List.of(
                anAlbum(anArtist()),
                anAlbum(anArtist())
        );

        List<AlbumDTO> randomAlbumDTOs = AlbumMapper.toDTOList(randomAlbums);
        given(albumService.getRandomAlbumsDTO(6)).willReturn(randomAlbumDTOs);

        MockHttpServletRequestBuilder request = get(URL_REGISTER);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_REGISTER))
                .andExpect(model().attribute(MODEL_REGISTER_DTO, org.hamcrest.Matchers.notNullValue()))
                .andExpect(model().attributeExists(MODEL_ALBUMS))
                .andExpect(model().attribute(MODEL_ALBUMS, org.hamcrest.Matchers.hasSize(2)));
    }

    @Test
    void doRegister_withValidData_shouldRegisterUserAndRedirectToLogin() throws Exception {

        MockHttpServletRequestBuilder request = post(URL_REGISTER)
                .with(csrf())
                .param("username", "testUser")
                .param("password", "password123")
                .param("confirmPassword", "password123")
                .param("country", "BULGARIA");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(userService).registerUser(any());
    }

    @Test
    void doRegister_withInvalidUsername_shouldReturnRegisterViewWithoutRegistering() throws Exception {

        MockHttpServletRequestBuilder request = post(URL_REGISTER)
                .with(csrf())
                .param("username", "ab") // Too short (min 3)
                .param("password", "pass123")
                .param("confirmPassword", "pass123")
                .param("country", "BULGARIA");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_REGISTER));

        verify(userService, never()).registerUser(any());
    }

    @Test
    void doRegister_withInvalidPassword_shouldReturnRegisterViewWithoutRegistering() throws Exception {

        MockHttpServletRequestBuilder request = post(URL_REGISTER)
                .with(csrf())
                .param("username", "testUser")
                .param("password", "ab") // Too short (min 3)
                .param("confirmPassword", "ab")
                .param("country", "BULGARIA");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_REGISTER));

        verify(userService, never()).registerUser(any());
    }

    @Test
    void doRegister_withNullCountry_shouldReturnRegisterViewWithoutRegistering() throws Exception {

        MockHttpServletRequestBuilder request = post(URL_REGISTER)
                .with(csrf())
                .param("username", "testUser")
                .param("password", "pass123")
                .param("confirmPassword", "pass123");
        // country param is missing

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_REGISTER));

        verify(userService, never()).registerUser(any());
    }

    @Test
    void doRegister_withMissingUsername_shouldReturnRegisterViewWithoutRegistering() throws Exception {

        MockHttpServletRequestBuilder request = post(URL_REGISTER)
                .with(csrf())
                .param("password", "pass123")
                .param("confirmPassword", "pass123")
                .param("country", "BULGARIA");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_REGISTER));

        verify(userService, never()).registerUser(any());
    }

    @Test
    void doRegister_withMissingPassword_shouldReturnRegisterViewWithoutRegistering() throws Exception {

        MockHttpServletRequestBuilder request = post(URL_REGISTER)
                .with(csrf())
                .param("username", "testUser")
                .param("confirmPassword", "pass123")
                .param("country", "BULGARIA");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_REGISTER));

        verify(userService, never()).registerUser(any());
    }
}
