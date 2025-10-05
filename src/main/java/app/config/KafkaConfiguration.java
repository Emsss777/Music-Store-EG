package app.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static app.util.KafkaTopics.USER_REGISTERED_EVENT_V1;

@Configuration
public class KafkaConfiguration {

    @Bean
    public NewTopic newTopic() {

        return TopicBuilder.name(USER_REGISTERED_EVENT_V1).build();
    }
}