package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NonexistentException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapperImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserServiceImpl userService;
    private int itemId = 1;

    public Item findItemById(int itemId) {
        Optional<Item> optionalItem = itemRepository.findAll().stream()
                .filter(i -> i.getId() == itemId)
                .findFirst();
        if (optionalItem.isPresent()) {
            return optionalItem.get();
        } else {
            throw new NonexistentException("такой вещи нет!");
        }
    }

    public ItemDto addItem(ItemDto itemDto, int userId) {
        Item item = ItemMapperImpl.toItemWithCheck(itemDto, userService.findUserById(userId));
        item.setId(itemId);
        itemId++;
        itemRepository.save(item);
        return ItemMapperImpl.toItemDto(item);
    }

    public ItemDto updateItem(ItemDto itemDto, int userId, int itemId) {
        Item item = findItemById(itemId);
        if (item.getOwner().getId() != userId) {
            throw new NonexistentException("Обновлять информацию о вещи может только её владелец");
        }
        Item itemUpdate = ItemMapperImpl.toItemWithoutCheck(itemDto, userService.findUserById(userId));
        if (itemUpdate.getAvailable() != null) {
            item.setAvailable(itemUpdate.getAvailable());
        }
        if (itemUpdate.getDescription() != null) {
            item.setDescription(itemUpdate.getDescription());
        }
        if (itemUpdate.getName() != null) {
            item.setName(itemUpdate.getName());
        }
        return ItemMapperImpl.toItemDto(item);
    }

    public List<ItemDto> findAllItemsByUser(int userId) {
        userService.findUserById(userId);
        return itemRepository.findAll().stream()
                .filter(i -> i.getOwner().getId() == userId)
                .map(ItemMapperImpl::toItemDto)
                .collect(Collectors.toList());
    }

    public List<ItemDto> searchItem(String text) {
        if (text.isBlank() || text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRepository.findAll().stream()
                .filter(Item::getAvailable)
                .filter(i -> i.getDescription().toLowerCase().contains(text.toLowerCase())
                        || i.getName().toLowerCase().contains(text.toLowerCase()))
                .map(ItemMapperImpl::toItemDto)
                .collect(Collectors.toList());
    }
}
