package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapperImpl;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.util.List;

import static ru.practicum.shareit.item.controller.XHeaderUserId.X_SHARER_USER_ID;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemServiceImpl service;

    @PostMapping()
    public ItemDto addItem(@RequestBody ItemDto item, @RequestHeader(X_SHARER_USER_ID) int id) {
        return service.addItem(item, id);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto item, @RequestHeader(X_SHARER_USER_ID) int userId, @PathVariable int itemId) {
        return service.updateItem(item, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDto findItemById(@PathVariable int itemId) {
        return ItemMapperImpl.toItemDto(service.findItemById(itemId));
    }

    @GetMapping()
    public List<ItemDto> findAllItemsUser(@RequestHeader(X_SHARER_USER_ID) int userId) {
        return service.findAllItemsByUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchByText(@RequestParam String text) {
        return service.searchItem(text);
    }
}
