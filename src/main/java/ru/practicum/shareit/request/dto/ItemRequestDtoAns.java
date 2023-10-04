package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestDtoAns {
    @NotNull
    private int id;
    @NotNull
    private User requester;
    @NotNull
    @NotBlank
    private String description;
    @NotNull
    private LocalDateTime created;
    private List<ItemDto> items;
}
