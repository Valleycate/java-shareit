package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;

public class ItemMapperImpl {

    public static Item toItemWithCheck(@Valid ItemDto itemDto, User owner) {
        if (itemDto.getAvailable() == null || itemDto.getName() == null || itemDto.getDescription() == null || itemDto.getName().isBlank()) {
            throw new ValidationException("Статус, имя, описание, не могут быть пустыми у вещи");
        }
        return toItemWithoutCheck(itemDto, owner);
    }

    public static Item toItemWithoutCheck(@Valid ItemDto itemDto, User owner) {
        Item item = new Item();
        item.setAvailable(itemDto.getAvailable());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setOwner(owner);
        return item;
    }

    public static ItemDto toItemDto(@Valid Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }
}
