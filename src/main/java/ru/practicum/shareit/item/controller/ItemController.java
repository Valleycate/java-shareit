package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemServiceImpl service;

    @PostMapping()
    public Item addItem(@RequestBody ItemDto item, @RequestHeader("X-Sharer-User-Id") int id) {
        return service.addItem(item, id);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestBody ItemDto item, @RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int itemId) {
        return service.updateItem(item, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public Item findItemById(@PathVariable int itemId) {
        return service.findItemById(itemId);
    }

    @GetMapping()
    public List<Item> findAllItemsUser(@RequestHeader("X-Sharer-User-Id") int userId) {
        return service.findAllItemsByUser(userId);
    }

    @GetMapping("/search")
    public List<Item> searchByText(@RequestParam String text) {
        return service.searchItem(text);
    }
}
