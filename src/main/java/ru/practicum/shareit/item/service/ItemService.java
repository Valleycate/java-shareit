package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item findItemById(int itemId);

    ItemDto addItem(ItemDto itemDto, int userId);

    ItemDto updateItem(ItemDto itemDto, int userId, int itemId);

    List<ItemDto> findAllItemsByUser(int userId);

    List<ItemDto> searchItem(String text);
}
