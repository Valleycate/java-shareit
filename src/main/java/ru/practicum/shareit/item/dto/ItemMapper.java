package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;

@Mapper
public interface ItemMapper {

    Item toItemWithCheck(@Valid ItemDto itemDto, Integer ownerId);

    Item toItemWithoutCheck(ItemDto itemDto, Integer ownerId);

    ItemDto toItemDto(@Valid Item item);
}
