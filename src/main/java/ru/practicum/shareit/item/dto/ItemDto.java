package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ItemDto {
    private int id;
    @NotBlank
    @Size(min = 1, max = 100)
    @NotNull
    private String name;
    @NotBlank
    @Size(min = 1, max = 300)
    @NotNull
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
}
