package ru.practicum.shareit.booking.dto;

import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class BookingDtoRequest {
    private int id;
    @NotNull
    @FutureOrPresent
    private LocalDateTime end;
    @NotNull
    @FutureOrPresent
    private LocalDateTime start;
    @NotNull
    private Integer itemId;
}
