package app.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static app.util.FeignClients.NOTIFICATION_SVC;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = NOTIFICATION_SVC)
public class NotificationServiceProperties {

    private String baseUrl;
}
