package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    public List<User> findAll();

    public User addUser(UserDto userDto);

    public User findUserById(int userId);

    public User updateUser(UserDto userDto, int userId);

    public void deleteUser(int userId);
}
