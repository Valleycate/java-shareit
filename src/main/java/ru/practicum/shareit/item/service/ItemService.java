package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item findItemById(int itemId);

    Item addItem(ItemDto itemDto, int userId);

    Item updateItem(ItemDto itemDto, int userId, int itemId);

    public List<Item> findAllItemsByUser(int userId);

    public List<Item> searchItem(String text);
}
