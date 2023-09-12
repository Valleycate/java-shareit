package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class NextBooking {
    @NotNull
    private LocalDateTime start;
    @NotNull
    private int id;
    @NotNull
    private int bookerId;
}
