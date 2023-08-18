package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.DuplicateEmail;
import ru.practicum.shareit.exceptions.NonexistentException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    int userId = 1;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User addUser(UserDto userDto) {
        User user = UserMapper.toUserWithCheck(userDto);
        Optional<User> optionalUser = userRepository.findAll().stream()
                .filter(u -> u.getEmail().equals(user.getEmail()))
                .findFirst();
        if (optionalUser.isPresent()) {
            throw new DuplicateEmail("такой email уже используется");
        }
        user.setId(userId);
        userId++;
        userRepository.save(user);
        return user;
    }

    public User findUserById(int userId) {
        Optional<User> optionalUser = userRepository.findAll().stream()
                .filter(u -> u.getId() == userId)
                .findFirst();
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new NonexistentException("такого пользователя нет!");
        }
    }

    public User updateUser(UserDto userDto, int userId) {
        User user = findUserById(userId);
        User updateUser = UserMapper.toUserWithoutCheck(userDto);
        if (updateUser.getEmail() != null) {
            Optional<User> optionalUser = userRepository.findAll().stream()
                    .filter(u -> u.getId() != userId)
                    .filter(u -> u.getEmail().equals(updateUser.getEmail()))
                    .findFirst();
            if (optionalUser.isPresent()) {
                throw new DuplicateEmail("такой email уже используется");
            }
            user.setEmail(updateUser.getEmail());
        }
        if (updateUser.getName() != null) {
            user.setName(updateUser.getName());
        }
        userRepository.save(user);
        return user;
    }

    public void deleteUser(int userId) {
        userRepository.delete(userId);
    }
}
