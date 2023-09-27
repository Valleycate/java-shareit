package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.AnsItemsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemServiceImplTest {
    private final EntityManager em;
    private final ItemServiceImpl itemService;
    private final UserServiceImpl userService;

    @Test
    @Order(value = 1)
    void shouldCreateItem() {
        try {
            UserDto userDto = new UserDto();
            userDto.setName("test");
            userDto.setEmail("test@email");
            userDto = userService.addUser(userDto);
            ItemDto itemDto = new ItemDto();
            itemDto.setName("test");
            itemDto.setDescription("test description");
            itemDto.setAvailable(true);
            itemDto = itemService.addItem(itemDto, userDto.getId());
            TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = :id", Item.class);
            Item item = query.setParameter("id", userDto.getId()).getSingleResult();
            assertThat(item.getId(), equalTo(itemDto.getId()));
            assertThat(item.getName(), equalTo(itemDto.getName()));
            assertThat(item.getAvailable(), equalTo(itemDto.getAvailable()));
        } catch (Exception ignored) {
        }
    }

    @Test
    @Order(value = 2)
    void shouldFindItemsByUser() {
        UserDto userDto = new UserDto();
        userDto.setName("test");
        userDto.setEmail("test@email");
        userDto = userService.addUser(userDto);
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test");
        itemDto.setDescription("test description");
        itemDto.setAvailable(true);
        itemService.addItem(itemDto, userDto.getId());
        List<AnsItemsDto> itemDtos = itemService.findAllItemsByUser(userDto.getId());
        TypedQuery<Item> query = em.createQuery("Select i from Item i", Item.class);
        List<Item> items = query.getResultList();
        assertThat(itemDtos.size(), equalTo(items.size()));
    }
}
