package ru.practicum.ewm.main.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record RestError(HttpStatus status, String reason, String message, LocalDateTime timestamp) {
}
