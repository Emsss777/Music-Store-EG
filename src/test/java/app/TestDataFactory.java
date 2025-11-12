package app;

import app.model.dto.CartItemDTO;
import app.model.dto.CheckoutDTO;
import app.model.entity.Album;
import app.model.entity.Artist;
import app.model.entity.User;
import app.model.enums.Country;
import app.model.enums.PrimaryGenre;
import app.model.enums.UserRole;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@UtilityClass
public class TestDataFactory {

    public static Artist anArtist() {

        return Artist.builder()
                .artistName("The Testers")
                .stageName("TT")
                .firstName("Test")
                .lastName("Artist")
                .artistBio("Legendary test artist.")
                .primaryGenre(PrimaryGenre.ROCK)
                .createdOn(LocalDateTime.now())
                .build();
    }

    public static Album anAlbum(Artist artist) {

        return Album.builder()
                .title("Integration Symphony")
                .genre(PrimaryGenre.ROCK)
                .year(2024)
                .description("A concept album for QA.")
                .coverUrl("https://example.com/cover.jpg")
                .price(new BigDecimal("19.99"))
                .artist(artist)
                .createdOn(LocalDateTime.now())
                .build();
    }

    public static User aUser() {

        return User.builder()
                .username("qa-user")
                .firstName("Quality")
                .lastName("Assurance")
                .email("qa@example.com")
                .password("encoded-password")
                .country(Country.BULGARIA)
                .role(UserRole.USER)
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
    }

    public static CheckoutDTO aCheckoutDTO() {

        return CheckoutDTO.builder()
                .firstName("Quality")
                .lastName("Assurance")
                .email("qa@example.com")
                .address("42 Test Lane")
                .city("Sofia")
                .state("SO")
                .zipCode("1000")
                .country("Bulgaria")
                .cardNumber("4111111111111111")
                .expiryDate("12/28")
                .cvv("123")
                .cardName("Quality Assurance")
                .build();
    }

    public static CartItemDTO aCartItem(Album album) {

        return CartItemDTO.builder()
                .albumId(album.getId() != null ? album.getId() : UUID.randomUUID())
                .title(album.getTitle())
                .artistName(album.getArtist().getArtistName())
                .coverUrl(album.getCoverUrl())
                .price(album.getPrice())
                .quantity(1)
                .build();
    }
}
