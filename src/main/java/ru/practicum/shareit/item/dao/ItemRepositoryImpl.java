package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class ItemRepositoryImpl implements ItemRepository {
    private HashMap<Integer, Item> items = new HashMap();

    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    public Item save(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    public void delete(int itemId) {
        items.remove(itemId);
    }
}
