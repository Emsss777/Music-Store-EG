package app.init.impl;

import app.exception.DomainException;
import app.init.DataSeeder;
import app.model.entity.UserEntity;
import app.model.enums.Country;
import app.model.enums.UserRole;
import app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static app.util.ExceptionMessages.*;
import static app.util.SuccessMessages.*;

@Slf4j
@Profile("dev")
@Component
@RequiredArgsConstructor
public class UserDataSeeder implements DataSeeder {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void seed() {

        seedAdmin();
        seedTestUser();
    }

    private void seedAdmin() {

        String username = "Emil777";

        try {
            userService.getUserByUsername(username);
            log.info(AnsiOutput.toString(AnsiColor.YELLOW, ADMIN_ALREADY_EXIST), username);
            return;
        } catch (DomainException ex) {
            log.info(AnsiOutput.toString(AnsiColor.BRIGHT_CYAN, ADMIN_NOT_FOUND), username);
        }

        UserEntity admin = new UserEntity()
                .setUsername(username)
                .setFirstName("Emil")
                .setLastName("Ganchev")
                .setEmail("admin@softuni.bg")
                .setPassword(passwordEncoder.encode("123456"))
                .setBirthDate(LocalDate.parse("1999-09-19"))
                .setCountry(Country.BULGARIA)
                .setRole(UserRole.ADMIN)
                .setActive(true)
                .setCreatedOn(LocalDateTime.now())
                .setUpdatedOn(LocalDateTime.now());

        userService.saveUser(admin);
        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_GREEN, SEEDED_ADMIN_USER), username);
    }

    private void seedTestUser() {

        String username = "Petko777";

        try {
            userService.getUserByUsername(username);
            log.info(AnsiOutput.toString(AnsiColor.YELLOW, TEST_USER_ALREADY_EXIST), username);
            return;
        } catch (DomainException ex) {
            log.info(AnsiOutput.toString(AnsiColor.BRIGHT_CYAN, TEST_USER_NOT_FOUND), username);
        }

        UserEntity admin = new UserEntity()
                .setUsername("Petko777")
                .setFirstName("Petko")
                .setLastName("Ganchev")
                .setEmail("user1@softuni.bg")
                .setPassword(passwordEncoder.encode("456123"))
                .setBirthDate(LocalDate.parse("1989-08-18"))
                .setCountry(Country.SPAIN)
                .setRole(UserRole.USER)
                .setActive(true)
                .setCreatedOn(LocalDateTime.now())
                .setUpdatedOn(LocalDateTime.now());

        userService.saveUser(admin);
        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_GREEN, SEEDED_TEST_USER), username);
    }

    @Override
    public int order() {
        return 5;
    }
}
