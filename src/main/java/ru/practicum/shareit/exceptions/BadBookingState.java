package ru.practicum.shareit.exceptions;

public class BadBookingState extends RuntimeException {

    public BadBookingState(String message) {
        super(message);
    }
}
