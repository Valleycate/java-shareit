package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.user.dao.UserDbRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    UserDbRepository repository = Mockito.mock(UserDbRepository.class);
    UserService service = new UserServiceImpl(repository);

    @Test
    void shouldAddUser() {
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setName("user1");
        userDto.setEmail("user1@email");
        assertThat(userDto, equalTo(service.addUser(userDto)));
    }

    @Test
    void shouldUpdateUser() {
        User user1 = new User();
        user1.setId(1);
        user1.setName("user1");
        user1.setEmail("user1@email");
        when(repository.findById(any()))
                .thenReturn(Optional.of(user1));
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setName("userDto");
        userDto.setEmail("userDto@email");
        service.addUser(userDto);
        assertThat(userDto, equalTo(service.updateUser(userDto, 1)));
        userDto.setEmail("user1@email");
        assertThat("user1@email", equalTo(service.updateUser(userDto, 1).getEmail()));
    }

    @Test
    void shouldDeleteUser() {
        User user1 = new User();
        user1.setId(1);
        user1.setName("user1");
        user1.setEmail("user1@email");
        when(repository.findById(any()))
                .thenReturn(Optional.of(user1));
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setName("userDto");
        userDto.setEmail("userDto@email");
        service.deleteUser(userDto.getId());
    }

}
