package ru.practicum.shareit.itemRequest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoAns;
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
public class ItemRequestServiceImplTest {
    private final ItemRequestServiceImpl itemRequestService;
    private final UserServiceImpl userService;
    private final EntityManager em;

    @Test
    void shouldFindRequestsByUserId() {
        UserDto userDto = new UserDto();
        userDto.setName("test");
        userDto.setEmail("test@email");
        userDto = userService.addUser(userDto);
        ItemRequestDto description = new ItemRequestDto();
        description.setDescription("item request");
        itemRequestService.addRequest(description, userDto.getId());
        description.setDescription("item request 2");
        itemRequestService.addRequest(description, userDto.getId());
        List<ItemRequestDtoAns> itemRequestList = itemRequestService.findRequestsByUserId(userDto.getId());
        TypedQuery<ItemRequest> query = em.createQuery("From requests", ItemRequest.class);
        List<ItemRequest> itemRequests = query.getResultList();
        assertThat(itemRequestList.size(), equalTo(itemRequests.size()));
    }
}
