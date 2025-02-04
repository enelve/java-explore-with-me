package ru.practicum.ewm.stats.server.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.DateTimeException;
import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class RestControllerAdvice {

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<RestError> handleException(DateTimeException e) {
        return processException(HttpStatus.BAD_REQUEST, "Incorrectly made request.", e.getMessage());
    }

    private ResponseEntity<RestError> processException(HttpStatus status, String reason, String message) {
        log.error("Client request error: {}", message);
        RestError restError = new RestError(status, reason, message, LocalDateTime.now());

        return ResponseEntity.status(status).body(restError);
    }
}
