package ru.practicum.shareit.exceptions;

public class BadBookingState extends RuntimeException {
    String error;

    public BadBookingState(String message) {
        super(message);
        error = message;
    }
}
