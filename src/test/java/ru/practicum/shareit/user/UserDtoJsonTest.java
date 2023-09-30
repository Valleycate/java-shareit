package ru.practicum.shareit.user;

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
    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    void testUserDto() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setName("test");
        userDto.setEmail("test@email");
        JsonContent<UserDto> result = json.write(userDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("test");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("test@email");
    }

    @Test
    void mapperUserDto() {
        UserDto originalUserDto = new UserDto();
        originalUserDto.setId(1);
        originalUserDto.setName("test");
        originalUserDto.setEmail("test@email");
        UserMapper mapper = Mappers.getMapper(UserMapper.class);
        assertNull(mapper.toUserWithCheck(null));
        assertNull(mapper.toUserWithoutCheck(null));
        User user = mapper.toUserWithoutCheck(originalUserDto);
        assertEquals(originalUserDto.getId(), user.getId());
        assertEquals(originalUserDto.getName(), user.getName());
        assertEquals(originalUserDto.getEmail(), user.getEmail());
        assertNull(mapper.toUserDto(null));
        UserDto userDto = mapper.toUserDto(user);
        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getEmail(), user.getEmail());
    }
}
