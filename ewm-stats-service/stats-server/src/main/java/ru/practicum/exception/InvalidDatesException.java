package ru.practicum.exception;

public class InvalidDatesException extends RuntimeException {
    public InvalidDatesException(String message) {
        super(message);
    }
}
