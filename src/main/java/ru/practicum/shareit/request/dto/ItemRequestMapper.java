package ru.practicum.shareit.request.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.validation.Valid;

@Mapper
public interface ItemRequestMapper {
    ItemRequestDtoAns toDto(@Valid ItemRequest request);

    ItemRequest fromDto(ItemRequestDtoAns request);
}
