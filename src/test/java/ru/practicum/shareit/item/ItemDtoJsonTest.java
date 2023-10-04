package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.AnsItemsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@JsonTest
public class ItemDtoJsonTest {
    private final ItemMapper mapper = Mappers.getMapper(ItemMapper.class);
    @Autowired
    private JacksonTester<ItemDto> json;
    private ItemDto itemDto;
    private User user;
    private Item item;

    @BeforeEach()
    void beforeEach() {
        itemDto = new ItemDto();
        itemDto.setDescription("description");
        itemDto.setName("name");
        itemDto.setId(1);
        itemDto.setAvailable(true);
        user = new User();
        user.setId(1);
        user.setName("user");
        user.setEmail("user@email");
        item = new Item();
        item.setOwner(user);
        item.setId(1);
        item.setAvailable(true);
        item.setDescription("description");
        item.setName("name");
    }

    @Test
    void itemDtoTest() throws Exception {
        JsonContent<ItemDto> result = json.write(itemDto);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
    }

    @Test
    void shouldReturnNotNull() {
        assertNotNull(mapper.toItemWithCheck(itemDto, null, null));
        assertNotNull(mapper.toItemWithCheck(null, new User(), null));
        assertNotNull(mapper.toItemWithCheck(null, null, new ItemRequest()));
        assertNotNull(mapper.toItemWithoutCheck(itemDto, null, null));
        assertNotNull(mapper.toItemWithoutCheck(null, null, new ItemRequest()));
        assertNotNull(mapper.toItemWithoutCheck(null, new User(), null));
        assertNotNull(mapper.toAnsItemsDto(null, new ArrayList<>()));
    }

    @Test
    void shouldReturnNull() {
        assertNull(mapper.toItemDto(null));
        assertNull(mapper.toItemWithCheck(null, null, null));
        assertNull(mapper.toItemWithoutCheck(null, null, null));
        assertNull(mapper.toAnsItemsDto(null, null));
    }

    @Test
    void shouldReturnItemWithoutCheck() {
        Item item = mapper.toItemWithoutCheck(itemDto, user, null);
        assertEqualsItemDtoToItem(itemDto, item);
    }

    @Test
    void shouldReturnItemWithCheck() {
        Item item = mapper.toItemWithCheck(itemDto, user, null);
        assertEqualsItemDtoToItem(itemDto, item);
    }

    @Test
    void shouldReturnAnsItemDto() {
        AnsItemsDto ansItemsDto = mapper.toAnsItemsDto(item, null);
        assertEquals(ansItemsDto.getDescription(), item.getDescription());
        assertEquals(ansItemsDto.getName(), item.getName());
        assertEquals(ansItemsDto.getId(), item.getId());
        assertEquals(ansItemsDto.getAvailable(), item.getAvailable());
    }

    @Test
    void shouldReturnItemDto() {
        itemDto = mapper.toItemDto(item);
        assertEqualsItemDtoToItem(itemDto, item);
        ItemRequest request = new ItemRequest();
        request.setId(1);
        item.setRequest(request);
        itemDto = mapper.toItemDto(item);
        assertEquals(itemDto.getRequestId(), item.getRequest().getId());
    }

    private void assertEqualsItemDtoToItem(ItemDto itemDto, Item item) {
        assertEquals(itemDto.getDescription(), item.getDescription());
        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getId(), item.getId());
        assertEquals(itemDto.getAvailable(), item.getAvailable());
    }
}
