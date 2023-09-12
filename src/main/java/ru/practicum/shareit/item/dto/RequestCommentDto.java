package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class RequestCommentDto {
    private int id;
    @NotNull
    @NotBlank
    @NotEmpty
    private String text;
}
