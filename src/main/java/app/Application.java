package app;

import app.config.NotificationServiceProperties;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableConfigurationProperties(NotificationServiceProperties.class)
@EnableFeignClients
@EnableCaching
@SpringBootApplication
public class Application {

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);
	}

    @PostConstruct
    public void initAnsi() {

        AnsiOutput.setEnabled(AnsiOutput.Enabled.ALWAYS);
    }
}
