package ru.practicum.shareit.booking.dto;

import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class BookingDtoRequest {
    private final int id;
    @NotNull
    @FutureOrPresent
    private final LocalDateTime end;
    @NotNull
    @FutureOrPresent
    private final LocalDateTime start;
    @NotNull
    private final Integer itemId;
}
