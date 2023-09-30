package ru.practicum.shareit.itemRequest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exceptions.NonexistentException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoAns;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemRequestServiceImplTest {
    private final ItemRequestServiceImpl itemRequestService;
    private final UserServiceImpl userService;
    private final EntityManager em;
    private UserDto userDto;
    private ItemRequestDto description;

    @BeforeEach
    void beforeEach() {
        userDto = new UserDto();
        userDto.setName("test");
        userDto.setEmail("test@email");
        userDto = userService.addUser(userDto);
        description = new ItemRequestDto();
        description.setDescription("item request");
        itemRequestService.addRequest(description, userDto.getId());
    }

    @Test
    void shouldFindRequestsByUserId() {
        description.setDescription("item request 2");
        itemRequestService.addRequest(description, userDto.getId());
        List<ItemRequestDtoAns> itemRequestList = itemRequestService.findRequestsByUserId(userDto.getId());
        TypedQuery<ItemRequest> query = em.createQuery("From requests", ItemRequest.class);
        List<ItemRequest> itemRequests = query.getResultList();
        assertThat(itemRequestList.size(), equalTo(itemRequests.size()));
    }

    @Test
    void shouldFindRequestsById() {
        try {
            itemRequestService.findRequestById(-1, userDto.getId());
        } catch (NonexistentException e) {
            assertThat(e.getMessage(), equalTo("запроса по такому id нет!"));
        }
        ItemRequestDtoAns addRequest = itemRequestService.addRequest(description, userDto.getId());
        addRequest.setItems(new ArrayList<>());
        ItemRequestDtoAns findItemRequest = itemRequestService.findRequestById(addRequest.getId(), userDto.getId());
        assertThat(addRequest, equalTo(findItemRequest));
        TypedQuery<ItemRequest> query = em.createQuery("From requests i where i.id = :id", ItemRequest.class);
        ItemRequest itemRequest = query.setParameter("id", addRequest.getId()).getSingleResult();
        assertThat(addRequest.getId(), equalTo(itemRequest.getId()));
        assertThat(addRequest.getDescription(), equalTo(itemRequest.getDescription()));
        assertThat(addRequest.getRequester(), equalTo(itemRequest.getRequester()));
        assertThat(addRequest.getCreated(), equalTo(itemRequest.getCreated()));
    }

    @Test
    void shouldFindAllRequests() {
        UserDto user = new UserDto();
        user.setName("user");
        user.setEmail("user@email");
        user = userService.addUser(user);
        description.setDescription("item request 2");
        itemRequestService.addRequest(description, userDto.getId());
        List<ItemRequestDtoAns> itemRequestList = itemRequestService.findAllRequests(0, 20, user.getId());
        try {
            itemRequestService.findAllRequests(-1, 10, userDto.getId());
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Page index must not be less than zero"));
        }
        TypedQuery<ItemRequest> query = em.createQuery("From requests", ItemRequest.class);
        List<ItemRequest> itemRequests = query.getResultList();
        assertThat(itemRequestList.size(), equalTo(itemRequests.size()));
        description.setDescription("item request 3");
        itemRequestService.addRequest(description, user.getId());
        itemRequestList = itemRequestService.findAllRequests(0, 20, user.getId());
        assertThat(itemRequestList.size(), equalTo(itemRequests.size()));
    }
}
