package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exceptions.BadBookingState;
import ru.practicum.shareit.exceptions.ErrorHandler;
import ru.practicum.shareit.exceptions.NonexistentException;
import ru.practicum.shareit.exceptions.ValidationException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorHandlerTest {
    ErrorHandler errorHandler = new ErrorHandler();

    @Test
    void handleBadBookingState() {
        assertEquals("Unknown state: test", errorHandler.handleBadBookingState(new BadBookingState("test")).getError());
    }

    @Test
    void handleNonexistentException() {
        assertEquals(Map.of("Non-existent object", "test"), errorHandler.handleNonexistentException(new NonexistentException("test")));
    }

    @Test
    void handleValidationExceptions() {
        assertEquals(Map.of("Bad validation", "test"), errorHandler.handleValidationExceptions(new ValidationException("test")));
    }
}
