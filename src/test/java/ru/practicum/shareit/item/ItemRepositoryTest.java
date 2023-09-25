package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.dao.ItemDbRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDbRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    UserDbRepository userRepository;
    @Autowired
    ItemDbRepository itemRepository;
    User user1;
    Item item1;
    User user2;
    Item item2;
    Item item3;

    @BeforeEach
    void beforeEach() {
        user1 = new User();
        user1.setId(1);
        user1.setName("user1");
        user1.setEmail("user1@email");
        user1 = userRepository.save(user1);

        item1 = new Item();
        item1.setId(1);
        item1.setOwner(user1);
        item1.setName("item 1");
        item1.setDescription("item 1 description");
        item1.setAvailable(true);
        item1 = itemRepository.save(item1);

        user2 = new User();
        user2.setId(2);
        user2.setName("user2");
        user2.setEmail("user2@email");
        user2 = userRepository.save(user2);

        item2 = new Item();
        item2.setId(2);
        item2.setOwner(user2);
        item2.setName("item 2");
        item2.setDescription("item 2 description");
        item2.setAvailable(true);
        item2 = itemRepository.save(item2);

        item3 = new Item();
        item3.setId(3);
        item3.setOwner(user2);
        item3.setName("item 3");
        item3.setDescription("item 3 description");
        item3.setAvailable(true);
        item3 = itemRepository.save(item3);
    }

    @Test
    void shouldFindAllItemsByUser() {
        final List<Item> byOwner = itemRepository.findAllByOwnerId(user1.getId());
        assertEquals(0, byOwner.size());
        final List<Item> allByOwner = itemRepository.findAllByOwnerId(user2.getId());
        assertEquals(2, allByOwner.size());
    }
}
