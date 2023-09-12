package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NonexistentException;
import ru.practicum.shareit.user.dao.UserDbRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserDbRepository repository;
    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    public List<UserDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDto addUser(UserDto userDto) {
        User user = mapper.toUserWithCheck(userDto);
        repository.save(user);
        return mapper.toUserDto(user);
    }

    public User findUserById(int userId) {
        Optional<User> optionalUser = repository.findById(userId);
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
            user.setEmail(updateUser.getEmail());
        }
        if (updateUser.getName() != null) {
            user.setName(updateUser.getName());
        }
        repository.save(user);
        return mapper.toUserDto(user);
    }

    public void deleteUser(int userId) {
        repository.delete(findUserById(userId));
    }
}
