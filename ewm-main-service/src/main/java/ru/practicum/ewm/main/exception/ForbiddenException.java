package ru.practicum.ewm.main.exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(final String message) {
        super(message);
    }
}
