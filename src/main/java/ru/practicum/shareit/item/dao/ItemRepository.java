package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    public List<Item> findAll();

    public Item save(Item item);

    public void delete(int itemId);
}
