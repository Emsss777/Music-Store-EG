package app.event;

import app.event.payload.UserUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static app.util.KafkaTopics.USER_UPDATED_EVENT_V1;
import static app.util.LogMessages.USER_REGISTERED_EVENT_PUBLISHED;
import static app.util.LogMessages.USER_UPDATED_EVENT_PUBLISHED;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserUpdatedEventProducer {

    private final KafkaTemplate<String, UserUpdatedEvent> kafkaTemplate;

    public void sendEvent(UserUpdatedEvent event) {
        kafkaTemplate.send(USER_UPDATED_EVENT_V1, event);
        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_GREEN, USER_UPDATED_EVENT_PUBLISHED), event.getUserId());
    }
}
