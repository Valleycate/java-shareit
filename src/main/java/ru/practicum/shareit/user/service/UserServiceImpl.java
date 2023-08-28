package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.DuplicateEmail;
import ru.practicum.shareit.exceptions.NonexistentException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    UserMapper mapper = Mappers.getMapper(UserMapper.class);
    private final UserRepository userRepository;
    private int userId = 1;

    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(mapper::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDto addUser(UserDto userDto) {
        User user = mapper.toUserWithCheck(userDto);
        Optional<User> optionalUser = userRepository.findAll().stream()
                .filter(u -> u.getEmail().equals(user.getEmail()))
                .findFirst();
        if (optionalUser.isPresent()) {
            throw new DuplicateEmail("такой email уже используется");
        }
        user.setId(userId);
        userId++;
        userRepository.save(user);
        return mapper.toUserDto(user);
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

    public UserDto updateUser(UserDto userDto, int userId) {
        User user = findUserById(userId);
        User updateUser = mapper.toUserWithoutCheck(userDto);
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
        return mapper.toUserDto(user);
    }

    public void deleteUser(int userId) {
        userRepository.delete(userId);
    }
}
