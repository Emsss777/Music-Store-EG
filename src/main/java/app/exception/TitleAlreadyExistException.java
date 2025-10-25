package app.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TitleAlreadyExistException extends RuntimeException  {

    public TitleAlreadyExistException(String message) {
        super(message);
    }
}
