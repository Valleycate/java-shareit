package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Integer, User> users = new HashMap<>();

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public User save(User user) {
        users.put(user.getId(), user);
        return user;
    }

    public void delete(int userId) {
        users.remove(userId);
    }
}
