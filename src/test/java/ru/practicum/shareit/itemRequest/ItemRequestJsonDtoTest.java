package ru.practicum.shareit.itemRequest;

import org.junit.jupiter.api.BeforeEach;
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
    private final ItemRequestMapper mapper = Mappers.getMapper(ItemRequestMapper.class);
    @Autowired
    private JacksonTester<ItemRequestDto> json;
    private ItemRequest itemRequest;
    private ItemRequestDtoAns itemRequestDtoAns;

    @BeforeEach
    void beforeEach() {
        User user = new User();
        user.setId(1);
        user.setName("user");
        user.setEmail("user@email");
        itemRequest = new ItemRequest();
        itemRequest.setRequester(user);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setDescription("item request");
        itemRequest.setId(1);
        itemRequestDtoAns = new ItemRequestDtoAns();
        itemRequestDtoAns.setRequester(user);
        itemRequestDtoAns.setCreated(LocalDateTime.now());
        itemRequestDtoAns.setDescription("item request");
        itemRequestDtoAns.setId(1);
    }

    @Test
    void testItemRequestDto() throws Exception {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("description");
        JsonContent<ItemRequestDto> result = json.write(requestDto);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
    }

    @Test
    void shouldReturnNull() {
        assertNull(mapper.toDto(null));
        assertNull(mapper.fromDto(null));
    }

    @Test
    void shouldReturnItemRequest() {
        ItemRequest request = mapper.fromDto(itemRequestDtoAns);
        assertEquals(request.getId(), itemRequestDtoAns.getId());
        assertEquals(request.getRequester(), itemRequestDtoAns.getRequester());
        assertEquals(request.getCreated(), itemRequestDtoAns.getCreated());
        assertEquals(request.getDescription(), itemRequestDtoAns.getDescription());
    }

    @Test
    void shouldReturnItemRequestDto() {
        ItemRequestDtoAns itemRequestDtoAns = mapper.toDto(itemRequest);
        assertEquals(itemRequest.getId(), itemRequestDtoAns.getId());
        assertEquals(itemRequest.getRequester(), itemRequestDtoAns.getRequester());
        assertEquals(itemRequest.getCreated(), itemRequestDtoAns.getCreated());
        assertEquals(itemRequest.getDescription(), itemRequestDtoAns.getDescription());
    }

}
