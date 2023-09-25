package ru.practicum.shareit.itemRequest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.request.dao.RequestDbRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserDbRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ItemRequestRepositoryTest {
    @Autowired
    RequestDbRepository requestRepository;
    @Autowired
    UserDbRepository userRepository;

    @Test
    void shouldFindAllByRequesterId() {
        User user = new User();
        user.setId(1);
        user.setName("user");
        user.setEmail("user@email");
        userRepository.save(user);
        ItemRequest request = new ItemRequest();
        request.setRequester(user);
        request.setCreated(LocalDateTime.now());
        request.setDescription("item request");
        request.setId(1);
        requestRepository.save(request);
        List<ItemRequest> itemRequests = requestRepository.findAllByRequesterId(user.getId());
        assertEquals(1, itemRequests.size());
    }
}
