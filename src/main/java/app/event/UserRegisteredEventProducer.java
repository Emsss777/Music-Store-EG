package app.event;

import app.event.payload.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static app.util.KafkaTopics.USER_REGISTERED_EVENT_V1;
import static app.util.SuccessMessages.USER_REGISTERED_EVENT_PUBLISHED;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRegisteredEventProducer {

    private final KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate;

    public void sendEvent(UserRegisteredEvent event) {

        kafkaTemplate.send(USER_REGISTERED_EVENT_V1, event);
        log.info(USER_REGISTERED_EVENT_PUBLISHED, event.getUserId());
    }
}
