package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<UserDto> findAll();

    UserDto addUser(UserDto userDto);

    User findUserById(int userId);

    UserDto updateUser(UserDto userDto, int userId);

    void deleteUser(int userId);
}
