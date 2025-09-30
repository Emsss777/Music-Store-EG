package app.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UsernameAlreadyExistException extends RuntimeException {

    public UsernameAlreadyExistException(String message) {
        super(message);
    }
}
