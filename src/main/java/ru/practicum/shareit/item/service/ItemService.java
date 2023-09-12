package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.AnsItemsDto;
import ru.practicum.shareit.item.dto.AnswerCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.RequestCommentDto;

import java.util.List;

public interface ItemService {
    AnsItemsDto findItem(int itemId, int userId);

    ItemDto addItem(ItemDto itemDto, int userId);

    ItemDto updateItem(ItemDto itemDto, int userId, int itemId);

    List<AnsItemsDto> findAllItemsByUser(int userId);

    List<ItemDto> searchItem(String text);

    AnswerCommentDto addComment(RequestCommentDto commentDto, int itemId, int userId);
}
