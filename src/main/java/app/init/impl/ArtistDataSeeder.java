package app.init.impl;

import app.exception.DomainException;
import app.init.DataSeeder;
import app.model.entity.ArtistEntity;
import app.model.enums.PrimaryGenre;
import app.service.ArtistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static app.util.ExceptionMessages.*;
import static app.util.SuccessMessages.SEEDED_ARTIST;

@Slf4j
@Profile("dev")
@Component
@RequiredArgsConstructor
public class ArtistDataSeeder implements DataSeeder {

    private final ArtistService artistService;

    @Override
    @Transactional
    public void seed() {

        seedArtist1();
        seedArtist2();
        seedArtist3();
        seedArtist4();
        seedArtist5();
        seedArtist6();
    }

    private void seedArtist1() {

        String artistName = "Pink Floyd";

        try {
            artistService.getArtistByArtistName(artistName);
            log.info(AnsiOutput.toString(AnsiColor.YELLOW, ARTIST_ALREADY_EXIST), artistName);
            return;
        } catch (DomainException ex) {
            log.info(AnsiOutput.toString(AnsiColor.BRIGHT_CYAN, ARTIST_NOT_FOUND), artistName);
        }

        ArtistEntity artist1 = new ArtistEntity()
                .setArtistName(artistName)
                .setStageName("Pink Floyd")
                .setFirstName("David")
                .setLastName("Gilmour")
                .setArtistBio(
                        "Pink Floyd is a British rock band formed in 1965 in London. " +
                                "They became pioneers of progressive and psychedelic rock, " +
                                "famous for their conceptual albums, sonic experimentation, " +
                                "and groundbreaking live shows."
                )
                .setPrimaryGenre(PrimaryGenre.ROCK);

        artistService.saveArtist(artist1);
        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_GREEN, SEEDED_ARTIST), artistName);
    }

    private void seedArtist2() {

        String artistName = "Kendrick Lamar";

        try {
            artistService.getArtistByArtistName(artistName);
            log.info(AnsiOutput.toString(AnsiColor.YELLOW, ARTIST_ALREADY_EXIST), artistName);
            return;
        } catch (DomainException ex) {
            log.info(AnsiOutput.toString(AnsiColor.BRIGHT_CYAN, ARTIST_NOT_FOUND), artistName);
        }

        ArtistEntity artist2 = new ArtistEntity()
                .setArtistName(artistName)
                .setStageName("Kendrick Lamar")
                .setFirstName("Kendrick")
                .setLastName("Duckworth")
                .setArtistBio(
                        "Kendrick Lamar is an American rapper and songwriter born in 1987 in Compton, " +
                                "California. Known for his intellectual and emotional lyrics, " +
                                "he’s won numerous Grammy Awards and is the first rapper to " +
                                "receive the Pulitzer Prize for Music."
                )
                .setPrimaryGenre(PrimaryGenre.HIP_HOP);

        artistService.saveArtist(artist2);
        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_GREEN, SEEDED_ARTIST), artistName);
    }

    private void seedArtist3() {

        String artistName = "Ton Koopman";

        try {
            artistService.getArtistByArtistName(artistName);
            log.info(AnsiOutput.toString(AnsiColor.YELLOW, ARTIST_ALREADY_EXIST), artistName);
            return;
        } catch (DomainException ex) {
            log.info(AnsiOutput.toString(AnsiColor.BRIGHT_CYAN, ARTIST_NOT_FOUND), artistName);
        }

        ArtistEntity artist3 = new ArtistEntity()
                .setArtistName(artistName)
                .setStageName("Ton Koopman")
                .setFirstName("Ton")
                .setLastName("Koopman")
                .setArtistBio(
                        "Ton Koopman is a Dutch conductor, organist, and harpsichordist known for " +
                                "his vibrant and historically informed performances of Baroque music. " +
                                "He founded the Amsterdam Baroque Orchestra & Choir and is especially " +
                                "acclaimed for his interpretations of J.S. Bach’s works, " +
                                "performed with energy, precision, and authenticity."
                )
                .setPrimaryGenre(PrimaryGenre.CLASSICAL);

        artistService.saveArtist(artist3);
        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_GREEN, SEEDED_ARTIST), artistName);
    }

    private void seedArtist4() {

        String artistName = "Taylor Swift";

        try {
            artistService.getArtistByArtistName(artistName);
            log.info(AnsiOutput.toString(AnsiColor.YELLOW, ARTIST_ALREADY_EXIST), artistName);
            return;
        } catch (DomainException ex) {
            log.info(AnsiOutput.toString(AnsiColor.BRIGHT_CYAN, ARTIST_NOT_FOUND), artistName);
        }

        ArtistEntity artist4 = new ArtistEntity()
                .setArtistName(artistName)
                .setStageName("Taylor Swift")
                .setFirstName("Taylor")
                .setLastName("Swift")
                .setArtistBio(
                        "Taylor Swift is an American singer-songwriter born in 1989. " +
                                "Known for her autobiographical lyrics and genre versatility, " +
                                "she has evolved from country roots to become one of the most " +
                                "successful pop and indie artists in the world."
                )
                .setPrimaryGenre(PrimaryGenre.POP);

        artistService.saveArtist(artist4);
        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_GREEN, SEEDED_ARTIST), artistName);
    }

    private void seedArtist5() {

        String artistName = "Metallica";

        try {
            artistService.getArtistByArtistName(artistName);
            log.info(AnsiOutput.toString(AnsiColor.YELLOW, ARTIST_ALREADY_EXIST), artistName);
            return;
        } catch (DomainException ex) {
            log.info(AnsiOutput.toString(AnsiColor.BRIGHT_CYAN, ARTIST_NOT_FOUND), artistName);
        }

        ArtistEntity artist5 = new ArtistEntity()
                .setArtistName(artistName)
                .setStageName("Metallica")
                .setFirstName("James")
                .setLastName("Hetfield")
                .setArtistBio(
                        "Metallica is an American heavy metal band formed in 1981 in Los Angeles. " +
                                "Considered one of the “Big Four” of thrash metal, " +
                                "they are known for their intense live performances and " +
                                "enduring influence on the genre."
                )
                .setPrimaryGenre(PrimaryGenre.METAL);

        artistService.saveArtist(artist5);
        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_GREEN, SEEDED_ARTIST), artistName);
    }

    private void seedArtist6() {

        String artistName = "Daft Punk";

        try {
            artistService.getArtistByArtistName(artistName);
            log.info(AnsiOutput.toString(AnsiColor.YELLOW, ARTIST_ALREADY_EXIST), artistName);
            return;
        } catch (DomainException ex) {
            log.info(AnsiOutput.toString(AnsiColor.BRIGHT_CYAN, ARTIST_NOT_FOUND), artistName);
        }

        ArtistEntity artist6 = new ArtistEntity()
                .setArtistName(artistName)
                .setStageName("Daft Punk")
                .setFirstName("Thomas")
                .setLastName("Bangalter")
                .setArtistBio(
                        "Daft Punk was a French electronic music duo consisting of Thomas Bangalter and " +
                                "Guy-Manuel de Homem-Christo. They revolutionized modern dance and " +
                                "electronic music, known for their robotic helmets and mysterious persona."
                )
                .setPrimaryGenre(PrimaryGenre.ELECTRONIC);

        artistService.saveArtist(artist6);
        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_GREEN, SEEDED_ARTIST), artistName);
    }

    @Override
    public int order() {
        return 10;
    }
}
