package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.booking.dao.BookingDbRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dao.CommentDbRepository;
import ru.practicum.shareit.item.dao.ItemDbRepository;
import ru.practicum.shareit.item.dto.AnswerCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.RequestCommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class ItemServiceTest {
    ItemDbRepository itemRepository = Mockito.mock(ItemDbRepository.class);
    UserServiceImpl userService = Mockito.mock(UserServiceImpl.class);
    BookingDbRepository bookingDbRepository = Mockito.mock(BookingDbRepository.class);
    ItemRequestService itemRequestService = Mockito.mock(ItemRequestService.class);
    CommentDbRepository commentDbRepository = Mockito.mock(CommentDbRepository.class);
    ItemService itemService = new ItemServiceImpl(userService, itemRepository,
            bookingDbRepository, itemRequestService, commentDbRepository);

    @Test
    void shouldAddItem() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1);
        itemDto.setName("item 1");
        itemDto.setDescription("item 1 description");
        itemDto.setAvailable(true);
        assertThat(itemDto, equalTo(itemService.addItem(itemDto, 1)));
    }

    @Test
    void shouldUpdateItem() {
        User user = new User();
        user.setId(1);
        user.setName("user");
        user.setEmail("user@email");
        Item item = new Item();
        item.setId(1);
        item.setName("item 1");
        item.setDescription("item 1 description");
        item.setOwner(user);
        item.setAvailable(true);
        when(userService.findUserById(anyInt()))
                .thenReturn(user);
        when(itemRepository.findById(any()))
                .thenReturn(Optional.of(item));
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1);
        itemDto.setName("itemDto");
        itemDto.setDescription("itemDto description");
        itemDto.setAvailable(true);
        assertThat(itemDto, equalTo(itemService.updateItem(itemDto, 1, 1)));
    }

    @Test
    void shouldAddComment() {
        User user = new User();
        user.setId(1);
        user.setName("user");
        user.setEmail("user@email");
        Item item = new Item();
        item.setId(1);
        item.setName("item 1");
        item.setDescription("item 1 description");
        item.setAvailable(true);
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setEnd(LocalDateTime.MIN);
        when(bookingDbRepository.findByBooker_IdOrderByStartDesc(anyInt()))
                .thenReturn(List.of(booking));
        when(bookingDbRepository.findByItem_IdOrderByStartDesc(anyInt()))
                .thenReturn(List.of(booking));
        when(itemRepository.findById(any()))
                .thenReturn(Optional.of(item));
        when(userService.findUserById(anyInt()))
                .thenReturn(user);
        RequestCommentDto request = new RequestCommentDto();
        request.setId(1);
        request.setText("text");
        AnswerCommentDto answerCommentDto = itemService.addComment(request, 1, 1);
        assertThat(request.getId(), equalTo(answerCommentDto.getId()));
        assertThat(request.getText(), equalTo(answerCommentDto.getText()));
        assertThat(user.getName(), equalTo(answerCommentDto.getAuthorName()));
    }
}
