package ru.practicum.exceptions;

public class SqlConflictException extends RuntimeException {

    public SqlConflictException(String message) {
        super(message);
    }
}
