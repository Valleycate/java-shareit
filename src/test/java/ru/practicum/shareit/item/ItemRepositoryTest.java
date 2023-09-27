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
    User user;
    Item item1;
    Item item2;

    @BeforeEach
    void beforeEach() {
        user = new User();
        user.setId(2);
        user.setName("user");
        user.setEmail("user@email");
        user = userRepository.save(user);
        item1 = new Item();
        item1.setId(2);
        item1.setOwner(user);
        item1.setName("item 1");
        item1.setDescription("item 1 description");
        item1.setAvailable(true);
        item1 = itemRepository.save(item1);
        item2 = new Item();
        item2.setId(3);
        item2.setOwner(user);
        item2.setName("item 2");
        item2.setDescription("item 2 description");
        item2.setAvailable(true);
        item2 = itemRepository.save(item2);
    }

    @Test
    void shouldFindAllItemsByUser() {
            final List<Item> allByOwner = itemRepository.findAllByOwnerId(user.getId());
            assertEquals(2, allByOwner.size());

    }
}
