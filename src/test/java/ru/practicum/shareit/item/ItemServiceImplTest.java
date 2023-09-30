package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exceptions.NonexistentException;
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
    private UserDto userDto;
    private ItemDto itemDto;

    @BeforeEach
    void beforeEach() {
        userDto = new UserDto();
        userDto.setName("test");
        userDto.setEmail("test@email");
        userDto = userService.addUser(userDto);
        itemDto = new ItemDto();
        itemDto.setName("test");
        itemDto.setDescription("test description");
        itemDto.setAvailable(true);
        itemDto = itemService.addItem(itemDto, userDto.getId());
    }

    @Test
    @Order(value = 1)
    void shouldCreateItem() {
        try {
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
        List<AnsItemsDto> itemDtos = itemService.findAllItemsByUser(userDto.getId());
        TypedQuery<Item> query = em.createQuery("Select i from Item i", Item.class);
        List<Item> items = query.getResultList();
        assertThat(itemDtos.size(), equalTo(items.size()));
    }

    @Test
    void shouldFindItem() {
        UserDto userDto2 = new UserDto();
        userDto2.setName("test2");
        userDto2.setEmail("test2@email");
        userDto2 = userService.addUser(userDto2);
        AnsItemsDto ansItemsDto = itemService.findItem(itemDto.getId(), userDto.getId());
        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = :id", Item.class);
        Item item = query.setParameter("id", itemDto.getId()).getSingleResult();
        assertThat(ansItemsDto.getId(), equalTo(item.getId()));
        assertThat(ansItemsDto.getAvailable(), equalTo(item.getAvailable()));
        assertThat(ansItemsDto.getDescription(), equalTo(item.getDescription()));
        assertThat(ansItemsDto.getName(), equalTo(item.getName()));
        ansItemsDto = itemService.findItem(itemDto.getId(), userDto2.getId());
        assertThat(ansItemsDto.getId(), equalTo(item.getId()));
        assertThat(ansItemsDto.getAvailable(), equalTo(item.getAvailable()));
        assertThat(ansItemsDto.getDescription(), equalTo(item.getDescription()));
        assertThat(ansItemsDto.getName(), equalTo(item.getName()));
    }

    @Test
    void shouldFindItemById() {
        AnsItemsDto ansItemsDto = itemService.findItem(itemDto.getId(), userDto.getId());
        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = :id", Item.class);
        Item item = query.setParameter("id", itemDto.getId()).getSingleResult();
        assertThat(ansItemsDto.getId(), equalTo(item.getId()));
        assertThat(ansItemsDto.getAvailable(), equalTo(item.getAvailable()));
        assertThat(ansItemsDto.getDescription(), equalTo(item.getDescription()));
        assertThat(ansItemsDto.getName(), equalTo(item.getName()));
        try {
            itemService.findItem(-101, userDto.getId());
        } catch (NonexistentException e) {
            assertThat(e.getMessage(), equalTo("такой вещи нет!"));
        }
    }

    @Test
    void shouldFindItemByString() {
        itemService.addItem(itemDto, userDto.getId());
        List<ItemDto> itemDtos = itemService.searchItem("test");
        TypedQuery<Item> query = em.createQuery("Select i from Item i", Item.class);
        List<Item> items = query.getResultList();
        assertThat(itemDtos.size(), equalTo(items.size()));
    }
}
