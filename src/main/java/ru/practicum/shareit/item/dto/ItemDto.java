package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ItemDto {
    private final int id;
    @NotBlank
    @Size(min = 1, max = 30)
    @NotNull
    private final String name;
    @NotBlank
    @Size(min = 1, max = 150)
    @NotNull
    @NotBlank
    private final String description;
    @NotNull
    private final Boolean available;
}
