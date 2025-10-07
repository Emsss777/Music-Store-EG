package app.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;

import java.util.List;

import static app.util.SuccessMessages.*;

@Slf4j
@Profile("dev")
@Component
@RequiredArgsConstructor
public class AppDataSeeder implements CommandLineRunner {

    private final List<DataSeeder> seeders;

    @Override
    public void run(String... args) {

        seeders.stream()
                .sorted(java.util.Comparator.comparingInt(DataSeeder::order))
                .forEach(seeder -> {
                    log.info(AnsiOutput.toString(AnsiColor.BRIGHT_GREEN, SEEDING), seeder.getClass().getSimpleName());
                    seeder.seed();
                });
        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_GREEN, SEEDING_COMPLETE));
    }
}
