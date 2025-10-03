package app.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DomainException extends RuntimeException{

    public DomainException(String message) {
        super(message);
    }
}
