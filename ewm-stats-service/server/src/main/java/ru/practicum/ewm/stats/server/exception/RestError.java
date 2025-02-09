package ru.practicum.ewm.stats.server.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record RestError(HttpStatus status, String reason, String message, LocalDateTime timestamp) {
}
