package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class LastBooking {
    @NotNull
    private LocalDateTime end;
    @NotNull
    private int id;
    @NotNull
    private int bookerId;
}
