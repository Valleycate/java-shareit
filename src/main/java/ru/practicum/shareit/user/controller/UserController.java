package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    UserMapper mapper = Mappers.getMapper(UserMapper.class);
    private final UserServiceImpl userService;

    @PostMapping()
    public UserDto addUser(@Valid @RequestBody UserDto user) {
        return userService.addUser(user);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@RequestBody UserDto user, @PathVariable int userId) {
        return userService.updateUser(user, userId);
    }

    @GetMapping("/{userId}")
    public UserDto findUserById(@PathVariable int userId) {
        return mapper.toUserDto(userService.findUserById(userId));
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
    }

    @GetMapping()
    public List<UserDto> findAll() {
        return userService.findAll();
    }
}
