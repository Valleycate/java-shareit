package ru.practicum.shareit.user.Dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll();

    User save(User user);

    void delete(int userId);
}
