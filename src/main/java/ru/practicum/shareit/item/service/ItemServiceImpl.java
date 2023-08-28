package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NonexistentException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    ItemMapper mapper = Mappers.getMapper(ItemMapper.class);
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
        Item item = mapper.toItemWithCheck(itemDto, userService.findUserById(userId).getId());
        item.setId(itemId);
        itemId++;
        itemRepository.save(item);
        return mapper.toItemDto(item);
    }

    public ItemDto updateItem(ItemDto itemDto, int userId, int itemId) {
        Item item = findItemById(itemId);
        if (item.getOwnerId() != userId) {
            throw new NonexistentException("Обновлять информацию о вещи может только её владелец");
        }
        Item itemUpdate = mapper.toItemWithoutCheck(itemDto, userService.findUserById(userId).getId());
        if (itemUpdate.getAvailable() != null) {
            item.setAvailable(itemUpdate.getAvailable());
        }
        if (itemUpdate.getDescription() != null) {
            item.setDescription(itemUpdate.getDescription());
        }
        if (itemUpdate.getName() != null) {
            item.setName(itemUpdate.getName());
        }
        return mapper.toItemDto(item);
    }

    public List<ItemDto> findAllItemsByUser(int userId) {
        userService.findUserById(userId);
        return itemRepository.findAll().stream()
                .filter(i -> i.getOwnerId() == userId)
                .map(mapper::toItemDto)
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
                .map(mapper::toItemDto)
                .collect(Collectors.toList());
    }
}
