package ru.practicum.ewm.main.exception;

public class IncorrectRequestException extends RuntimeException {
    public IncorrectRequestException(String message) {
        super(message);
    }
}
