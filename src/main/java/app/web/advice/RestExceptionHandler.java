package app.web.advice;

import app.exception.DomainException;
import app.model.dto.ApiErrorDTO;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiErrorDTO> handleDomainException(DomainException ex) {

        log.warn("⚠ Domain exception: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDTO> handleValidationException(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return buildResponse(HttpStatus.BAD_REQUEST, "Validation failed!", errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDTO> handleGenericException(Exception ex) {

        log.error("💥 Unexpected error!", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error!");
    }

    private ResponseEntity<ApiErrorDTO> buildResponse(HttpStatus status, String message) {
        return buildResponse(status, message, null);
    }

    private ResponseEntity<ApiErrorDTO> buildResponse(HttpStatus status, String message, Map<String, String> errors) {

        ApiErrorDTO errorResponse = ApiErrorDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .validationErrors(errors)
                .build();

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ApiErrorDTO> handleFeignException(FeignException ex) {

        ApiErrorDTO apiError = ApiErrorDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(ex.status())
                .error("Notification Service Unavailable!")
                .message(ex.getMessage())
                .build();

        log.warn("⚠️ [notification-svc] is unreachable: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(apiError);
    }
}
