package ru.practicum.ewm.main.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@ControllerAdvice
public class RestControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestError> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.error("Client request error: {}", errors);
        return processException(BAD_REQUEST, "Incorrectly made request.", errors.toString());
    }

    @ExceptionHandler(IncorrectRequestException.class)
    public ResponseEntity<RestError> handleException(IncorrectRequestException e) {
        return processException(HttpStatus.BAD_REQUEST, "Incorrectly made request.", e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<RestError> handleException(NotFoundException e) {
        return processException(HttpStatus.NOT_FOUND, "The required object was not found.", e.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<RestError> handleException(ForbiddenException e) {
        return processException(HttpStatus.FORBIDDEN, "For the requested operation the conditions are not met.", e.getMessage());
    }

    @ExceptionHandler({SQLIntegrityConstraintViolationException.class, UserRequestException.class,
            DataIntegrityViolationException.class})
    public ResponseEntity<RestError> handleException(RuntimeException e) {
        return processException(HttpStatus.CONFLICT, "Integrity constraint has been violated", e.getMessage());
    }

    private ResponseEntity<RestError> processException(HttpStatus status, String reason, String message) {
        log.error("Client request error: {}", message);
        RestError restError = new RestError(status, reason, message, LocalDateTime.now());

        return ResponseEntity.status(status).body(restError);
    }
}
