package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;

public class UserMapper {
    public static User toUserWithCheck(@Valid UserDto userDto) {
        if (userDto.getEmail() == null || userDto.getName() == null) {
            throw new ValidationException("Имя и email пользователя не могут быть пусты");
        }
        return toUserWithoutCheck(userDto);
    }

    public static User toUserWithoutCheck(@Valid UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }
}
