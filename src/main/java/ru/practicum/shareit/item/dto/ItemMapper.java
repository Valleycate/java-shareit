package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;

public class ItemMapper {
    public static Item toItemWithCheck(@Valid ItemDto itemDto) {
        if (itemDto.getAvailable() == null || itemDto.getName() == null || itemDto.getDescription() == null || itemDto.getName().isBlank()) {
            throw new ValidationException("Статус, имя, описание, не могут быть пустыми у вещи");
        }
        return toItemWithoutCheck(itemDto);
    }

    public static Item toItemWithoutCheck(@Valid ItemDto itemDto) {
        Item item = new Item();
        item.setAvailable(itemDto.getAvailable());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        return item;
    }
}
