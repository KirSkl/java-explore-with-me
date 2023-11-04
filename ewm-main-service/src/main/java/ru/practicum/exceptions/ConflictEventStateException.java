package ru.practicum.exceptions;

public class ConflictEventStateException extends RuntimeException {
    public ConflictEventStateException(String message) {
        super(message);
    }
}
