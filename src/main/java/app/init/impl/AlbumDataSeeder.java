package app.init.impl;

import app.exception.DomainException;
import app.init.DataSeeder;
import app.model.entity.Album;
import app.model.entity.Artist;
import app.model.enums.PrimaryGenre;
import app.service.AlbumService;
import app.service.ArtistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static app.util.ExceptionMessages.*;
import static app.util.SuccessMessages.*;

@Slf4j
@Profile("dev")
@Component
@RequiredArgsConstructor
public class AlbumDataSeeder implements DataSeeder {

    private final ArtistService artistService;
    private final AlbumService albumService;

    @Override
    @Transactional
    public void seed() {

        seedAlbum1();
        seedAlbum2();
        seedAlbum3();
        seedAlbum4();
        seedAlbum5();
        seedAlbum6();
    }

    private void seedAlbum1() {

        String albumTitle = "The Dark Side of the Moon";

        try {
            albumService.getAlbumByAlbumTitle(albumTitle);
            log.info(AnsiOutput.toString(AnsiColor.YELLOW, ALBUM_ALREADY_EXISTS_SKIPPING_SEEDING), albumTitle);
            return;
        } catch (DomainException ex) {
            log.info(AnsiOutput.toString(AnsiColor.BRIGHT_CYAN, ALBUM_NOT_FOUND), albumTitle);
        }

        Artist artist1 = artistService.getArtistByArtistName("Pink Floyd");

        Album album1 = new Album()
                .setTitle("The Dark Side of the Moon")
                .setGenre(PrimaryGenre.ROCK)
                .setYear(1973)
                .setDescription(
                        "One of the most influential albums in rock history, " +
                                "known for its philosophical themes, flawless sound engineering, " +
                                "and seamless transitions between songs."
                )
                .setCoverUrl("/images/album-covers/pink-floyd-dark-side.jpg")
                .setPrice(BigDecimal.valueOf(12.99))
                .setArtist(artist1);

        artist1.getAlbums().add(album1);

        albumService.saveAlbum(album1);
        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_GREEN, SEEDED_ALBUM), albumTitle);
    }

    private void seedAlbum2() {

        String albumTitle = "To Pimp a Butterfly";

        try {
            albumService.getAlbumByAlbumTitle(albumTitle);
            log.info(AnsiOutput.toString(AnsiColor.YELLOW, ALBUM_ALREADY_EXISTS_SKIPPING_SEEDING), albumTitle);
            return;
        } catch (DomainException ex) {
            log.info(AnsiOutput.toString(AnsiColor.BRIGHT_CYAN, ALBUM_NOT_FOUND), albumTitle);
        }

        Artist artist2 = artistService.getArtistByArtistName("Kendrick Lamar");

        Album album2 = new Album()
                .setTitle("To Pimp a Butterfly")
                .setGenre(PrimaryGenre.HIP_HOP)
                .setYear(2015)
                .setDescription(
                        "A deep, socially and politically charged album blending rap, " +
                                "jazz, soul, and funk. It’s regarded as a modern masterpiece " +
                                "exploring identity and racial inequality in America."
                )
                .setCoverUrl("/images/album-covers/kendrick-lamar-butterfly.jpg")
                .setPrice(BigDecimal.valueOf(19.99))
                .setArtist(artist2);

        artist2.getAlbums().add(album2);

        albumService.saveAlbum(album2);
        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_GREEN, SEEDED_ALBUM), albumTitle);
    }

    private void seedAlbum3() {

        String albumTitle = "Bach: Organ Works";

        try {
            albumService.getAlbumByAlbumTitle(albumTitle);
            log.info(AnsiOutput.toString(AnsiColor.YELLOW, ALBUM_ALREADY_EXISTS_SKIPPING_SEEDING), albumTitle);
            return;
        } catch (DomainException ex) {
            log.info(AnsiOutput.toString(AnsiColor.BRIGHT_CYAN, ALBUM_NOT_FOUND), albumTitle);
        }

        Artist artist3 = artistService.getArtistByArtistName("Ton Koopman");

        Album album3 = new Album()
                .setTitle("Bach: Organ Works")
                .setGenre(PrimaryGenre.CLASSICAL)
                .setYear(1995)
                .setDescription(
                        "Ton Koopman performs a selection of J.S. Bach’s most celebrated organ pieces, " +
                                "combining brilliant technique with expressive depth. " +
                                "Recorded on historic baroque organs, the album highlights " +
                                "the rich tonal colors and spiritual power of Bach’s music."
                )
                .setCoverUrl("/images/album-covers/koopman-organ-works.jpg")
                .setPrice(BigDecimal.valueOf(14.99))
                .setArtist(artist3);

        artist3.getAlbums().add(album3);

        albumService.saveAlbum(album3);
        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_GREEN, SEEDED_ALBUM), albumTitle);
    }

    private void seedAlbum4() {

        String albumTitle = "1989";

        try {
            albumService.getAlbumByAlbumTitle(albumTitle);
            log.info(AnsiOutput.toString(AnsiColor.YELLOW, ALBUM_ALREADY_EXISTS_SKIPPING_SEEDING), albumTitle);
            return;
        } catch (DomainException ex) {
            log.info(AnsiOutput.toString(AnsiColor.BRIGHT_CYAN, ALBUM_NOT_FOUND), albumTitle);
        }

        Artist artist4 = artistService.getArtistByArtistName("Taylor Swift");

        Album album4 = new Album()
                .setTitle("1989")
                .setGenre(PrimaryGenre.POP)
                .setYear(2014)
                .setDescription(
                        "This album marked Taylor Swift’s transition from country to pop. " +
                                "It features an 80s-inspired synth sound and " +
                                "introspective lyrics about love, fame, and self-discovery."
                )
                .setCoverUrl("/images/album-covers/taylor-swift-1989.jpg")
                .setPrice(BigDecimal.valueOf(16.99))
                .setArtist(artist4);

        artist4.getAlbums().add(album4);

        albumService.saveAlbum(album4);
        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_GREEN, SEEDED_ALBUM), albumTitle);
    }

    private void seedAlbum5() {

        String albumTitle = "Master of Puppets";

        try {
            albumService.getAlbumByAlbumTitle(albumTitle);
            log.info(AnsiOutput.toString(AnsiColor.YELLOW, ALBUM_ALREADY_EXISTS_SKIPPING_SEEDING), albumTitle);
            return;
        } catch (DomainException ex) {
            log.info(AnsiOutput.toString(AnsiColor.BRIGHT_CYAN, ALBUM_NOT_FOUND), albumTitle);
        }

        Artist artist5 = artistService.getArtistByArtistName("Metallica");

        Album album5 = new Album()
                .setTitle("Master of Puppets")
                .setGenre(PrimaryGenre.METAL)
                .setYear(1986)
                .setDescription(
                        "Metallica’s Master of Puppets is a landmark thrash metal album " +
                                "blending speed, power, and intricate songwriting. " +
                                "It explores themes of control and addiction and " +
                                "was the last record to feature bassist Cliff Burton."
                )
                .setCoverUrl("/images/album-covers/metallica-master-of-puppets.jpg")
                .setPrice(BigDecimal.valueOf(11.99))
                .setArtist(artist5);

        artist5.getAlbums().add(album5);

        albumService.saveAlbum(album5);
        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_GREEN, SEEDED_ALBUM), albumTitle);
    }

    private void seedAlbum6() {

        String albumTitle = "Discovery";

        try {
            albumService.getAlbumByAlbumTitle(albumTitle);
            log.info(AnsiOutput.toString(AnsiColor.YELLOW, ALBUM_ALREADY_EXISTS_SKIPPING_SEEDING), albumTitle);
            return;
        } catch (DomainException ex) {
            log.info(AnsiOutput.toString(AnsiColor.BRIGHT_CYAN, ALBUM_NOT_FOUND), albumTitle);
        }

        Artist artist6 = artistService.getArtistByArtistName("Daft Punk");

        Album album6 = new Album()
                .setTitle("Discovery")
                .setGenre(PrimaryGenre.ELECTRONIC)
                .setYear(2001)
                .setDescription(
                        "Discovery is Daft Punk’s groundbreaking electronic album that mixes house, " +
                                "disco, and funk influences. It’s known for its catchy melodies, " +
                                "robotic vocals, and hits like “One More Time” and “Harder, Better, Faster, Stronger."
                )
                .setCoverUrl("/images/album-covers/daft-punk-discovery.jpg")
                .setPrice(BigDecimal.valueOf(15.99))
                .setArtist(artist6);

        artist6.getAlbums().add(album6);

        albumService.saveAlbum(album6);
        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_GREEN, SEEDED_ALBUM), albumTitle);
    }

    @Override
    public int order() {
        return 15;
    }
}
