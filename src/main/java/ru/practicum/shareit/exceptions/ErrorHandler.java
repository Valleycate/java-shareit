package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@ControllerAdvice
@RestController
public class ErrorHandler {
    @ExceptionHandler(ru.practicum.shareit.exceptions.NonexistentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNonexistentException(final NonexistentException e) {
        return Map.of("Non-existent object", e.getMessage());
    }

    @ExceptionHandler(DuplicateEmail.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleDuplicateEmail(final DuplicateEmail e) {
        return Map.of("Duplicate email", e.getMessage());
    }

    @ExceptionHandler({ValidationException.class, NullPointerException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(final RuntimeException e) {
        return Map.of("Bad validation", e.getMessage());
    }
}
