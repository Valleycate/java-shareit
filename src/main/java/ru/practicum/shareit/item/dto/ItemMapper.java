package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;

@Mapper
public interface ItemMapper {
    @Mapping(target = "id", source = "itemDto.id")
    @Mapping(target = "name", source = "itemDto.name")
    @Mapping(target = "description", source = "itemDto.description")
    Item toItemWithCheck(@Valid ItemDto itemDto, @Valid User owner, @Valid ItemRequest request);

    @Mapping(target = "id", source = "itemDto.id")
    @Mapping(target = "name", source = "itemDto.name")
    @Mapping(target = "description", source = "itemDto.description")
    Item toItemWithoutCheck(ItemDto itemDto, User owner, ItemRequest request);
    @Mapping(target = "requestId", source = "item.request.id")
    ItemDto toItemDto(@Valid Item item);
    AnsItemsDto toAnsItemsDto(@Valid Item item, List<AnswerCommentDto> comments);
}
