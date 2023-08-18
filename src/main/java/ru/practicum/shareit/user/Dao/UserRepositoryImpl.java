package ru.practicum.shareit.user.Dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class UserRepositoryImpl implements UserRepository {
    private HashMap<Integer, User> users = new HashMap();

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
