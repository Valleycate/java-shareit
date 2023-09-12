package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Mapper
public interface CommentMapper {
    @Mapping(target = "id", source = "commentDto.id")
    Comment toComment(@Valid RequestCommentDto commentDto, LocalDateTime created, User author, Item item);

    @Mapping(target = "authorName", source = "comment.author.name")
    AnswerCommentDto toCommentDto(@Valid Comment comment);
}
