package ru.practicum.shareit.itemRequest;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoAns;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@JsonTest
public class ItemRequestJsonDtoTest {
    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    void testItemRequestDto() throws Exception {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("description");
        JsonContent<ItemRequestDto> result = json.write(requestDto);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
    }

    @Test
    void mapperItemRequestDto() {
        ItemRequestMapper mapper = Mappers.getMapper(ItemRequestMapper.class);
        assertNull(mapper.toDto(null));
        assertNull(mapper.fromDto(null));
        User user = new User();
        user.setId(1);
        user.setName("user");
        user.setEmail("user@email");
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequester(user);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setDescription("item request");
        itemRequest.setId(1);
        ItemRequestDtoAns itemRequestDtoAns = mapper.toDto(itemRequest);
        assertEquals(itemRequest.getId(), itemRequestDtoAns.getId());
        assertEquals(itemRequest.getRequester(), itemRequestDtoAns.getRequester());
        assertEquals(itemRequest.getCreated(), itemRequestDtoAns.getCreated());
        assertEquals(itemRequest.getDescription(), itemRequestDtoAns.getDescription());
        ItemRequest request = mapper.fromDto(itemRequestDtoAns);
        assertEquals(request.getId(), itemRequestDtoAns.getId());
        assertEquals(request.getRequester(), itemRequestDtoAns.getRequester());
        assertEquals(request.getCreated(), itemRequestDtoAns.getCreated());
        assertEquals(request.getDescription(), itemRequestDtoAns.getDescription());
    }

}
