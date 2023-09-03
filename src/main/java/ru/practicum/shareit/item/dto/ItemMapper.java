package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;

@Mapper
public interface ItemMapper {
    @Mapping(target = "id", source = "itemDto.id")
    @Mapping(target = "name", source = "itemDto.name")
    Item toItemWithCheck(@Valid ItemDto itemDto, @Valid User owner);

    @Mapping(target = "id", source = "itemDto.id")
    @Mapping(target = "name", source = "itemDto.name")
    Item toItemWithoutCheck(ItemDto itemDto, User owner);

    ItemDto toItemDto(@Valid Item item);

    AnsItemsDto toAnsItemsDto(@Valid Item item, List<AnswerCommentDto> comments);
}
