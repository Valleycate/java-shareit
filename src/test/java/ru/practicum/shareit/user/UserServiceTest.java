package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
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
    private final UserDbRepository repository = Mockito.mock(UserDbRepository.class);
    private final UserService service = new UserServiceImpl(repository);
    private UserDto userDto;
    private User user1;

    @BeforeEach()
    void beforeEach() {
        userDto = new UserDto();
        userDto.setName("userDto");
        userDto.setEmail("userDto@email");
        userDto = service.addUser(userDto);
        user1 = new User();
        user1.setId(0);
        user1.setName("user1");
        user1.setEmail("user1@email");
    }

    @Test
    void shouldAddUser() {
        assertThat(userDto, equalTo(service.addUser(userDto)));
    }

    @Test
    void shouldUpdateUser() {
        when(repository.findById(any()))
                .thenReturn(Optional.of(user1));
        assertThat(userDto, equalTo(service.updateUser(userDto, 1)));
        userDto.setEmail("user1@email");
        assertThat("user1@email", equalTo(service.updateUser(userDto, 1).getEmail()));
        assertThat(userDto, equalTo(service.updateUser(new UserDto(), 1)));
    }

    @Test
    void shouldDeleteUser() {
        when(repository.findById(any()))
                .thenReturn(Optional.of(user1));
        service.deleteUser(userDto.getId());
    }

}
