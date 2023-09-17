package ru.practicum.shareit.request.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.Valid;
import java.util.List;

@Mapper
public interface ItemRequestMapper {
    ItemRequestDtoAns toDto(@Valid ItemRequest request);
    ItemRequest fromDto(ItemRequestDtoAns request);
}
