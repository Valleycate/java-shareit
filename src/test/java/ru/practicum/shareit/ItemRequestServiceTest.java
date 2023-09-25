package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.item.dao.ItemDbRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.request.RequestDbRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoAns;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class ItemRequestServiceTest {
    RequestDbRepository repository = Mockito.mock(RequestDbRepository.class);
    UserService userService = Mockito.mock(UserService.class);
    ItemDbRepository itemRepository = Mockito.mock(ItemDbRepository.class);
    ItemRequestServiceImpl service = new ItemRequestServiceImpl(repository, userService, itemRepository);

    @Test
    void shouldAddRequest() {
        User user = new User();
        user.setId(2);
        user.setName("user");
        user.setEmail("user@email");
        Item item = new Item();
        item.setId(1);
        item.setName("item 1");
        item.setDescription("item 1 description");
        item.setOwner(user);
        item.setAvailable(true);
        ItemRequestDto description = new ItemRequestDto();
        description.setDescription("item request");
        when(userService.findUserById(anyInt()))
                .thenReturn(user);
        when(itemRepository.findById(anyInt()))
                .thenReturn(Optional.of(item));
        ItemRequestDtoAns request = new ItemRequestDtoAns();
        request.setDescription(description.getDescription());
        request.setRequester(user);
        request.setId(0);
        assertThat(request.getId(), equalTo(service.addRequest(description, user.getId()).getId()));
        assertThat(request.getRequester(), equalTo(service.addRequest(description, user.getId()).getRequester()));
        assertThat(request.getItems(), equalTo(service.addRequest(description, user.getId()).getItems()));
        assertThat(request.getDescription(), equalTo(service.addRequest(description, user.getId()).getDescription()));
    }
}
