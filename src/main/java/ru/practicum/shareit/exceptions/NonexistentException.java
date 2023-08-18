package ru.practicum.shareit.exceptions;

public class NonexistentException extends RuntimeException {
    public NonexistentException(String message) {
        super(message);
    }

    public NonexistentException() {
        super();
    }

}