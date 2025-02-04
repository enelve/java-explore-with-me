package ru.practicum.ewm.main.exception;

public class UserRequestException extends RuntimeException {
    public UserRequestException(String message) {
        super(message);
    }
}