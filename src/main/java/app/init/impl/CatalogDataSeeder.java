package app.init.impl;

import app.init.DataSeeder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Profile("dev")
@Component
@RequiredArgsConstructor
public class CatalogDataSeeder implements DataSeeder {

    @Override
    public void seed() {

    }

    @Override
    public int order() {
        return 30;
    }
}
