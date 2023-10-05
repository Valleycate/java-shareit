package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@JsonTest
public class UserDtoJsonTest {
    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);
    @Autowired
    private JacksonTester<UserDto> json;
    private UserDto userDto;
    private User user;

    @BeforeEach
    void beforeEach() {
        userDto = new UserDto();
        userDto.setId(1);
        userDto.setName("test");
        userDto.setEmail("test@email");
        user = new User();
        user.setId(1);
        user.setName("test");
        user.setEmail("test@email");
    }

    @Test
    void testUserDto() throws Exception {
        JsonContent<UserDto> result = json.write(userDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("test");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("test@email");
    }

    @Test
    void shouldReturnNull() {
        assertNull(mapper.toUserDto(null));
        assertNull(mapper.toUserWithCheck(null));
        assertNull(mapper.toUserWithoutCheck(null));
    }

    @Test
    void mapperUserDto() {
        UserDto userDto = mapper.toUserDto(user);
        assertUserDtoEqualsUser(userDto, user);
    }

    @Test
    void shouldReturnUser() {
        User user = mapper.toUserWithoutCheck(userDto);
        assertUserDtoEqualsUser(userDto, user);
    }

    private void assertUserDtoEqualsUser(UserDto userDto, User user) {
        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getEmail(), user.getEmail());
    }
}
