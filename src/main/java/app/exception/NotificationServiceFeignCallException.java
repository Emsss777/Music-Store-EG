package app.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotificationServiceFeignCallException extends RuntimeException {

    public NotificationServiceFeignCallException(String message) {
        super(message);
    }
}
