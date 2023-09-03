package ru.practicum.shareit.user.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;

@Mapper
public interface UserMapper {
    User toUserWithCheck(@Valid UserDto userDto);

    User toUserWithoutCheck(UserDto userDto);

    UserDto toUserDto(@Valid User userDto);
}
