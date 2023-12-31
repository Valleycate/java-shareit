package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class AnswerCommentDto {
    @NotNull
    private int id;
    @NotNull
    @NotBlank
    private String text;
    @NotNull
    private LocalDateTime created;
    @NotNull
    @NotBlank
    private String authorName;
}
