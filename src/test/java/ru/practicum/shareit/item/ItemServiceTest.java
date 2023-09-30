package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.booking.dao.BookingDbRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.NonexistentException;
import ru.practicum.shareit.item.dao.CommentDbRepository;
import ru.practicum.shareit.item.dao.ItemDbRepository;
import ru.practicum.shareit.item.dto.AnswerCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.RequestCommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDtoAns;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class ItemServiceTest {
    private final ItemDbRepository itemRepository = Mockito.mock(ItemDbRepository.class);
    private final UserServiceImpl userService = Mockito.mock(UserServiceImpl.class);
    private final BookingDbRepository bookingDbRepository = Mockito.mock(BookingDbRepository.class);
    private final ItemRequestService itemRequestService = Mockito.mock(ItemRequestService.class);
    private final CommentDbRepository commentDbRepository = Mockito.mock(CommentDbRepository.class);
    private final ItemService itemService = new ItemServiceImpl(userService, itemRepository,
            bookingDbRepository, itemRequestService, commentDbRepository);
    private ItemDto itemDto;
    private User user;
    private Item item;
    private ItemRequestDtoAns description;

    @BeforeEach()
    void beforeEach() {
        itemDto = new ItemDto();
        itemDto.setId(1);
        itemDto.setName("item 1");
        itemDto.setDescription("item 1 description");
        itemDto.setAvailable(true);
        user = new User();
        user.setId(1);
        user.setName("user");
        user.setEmail("user@email");
        item = new Item();
        item.setId(1);
        item.setName("item 1");
        item.setDescription("item 1 description");
        item.setOwner(user);
        item.setAvailable(true);
        description = new ItemRequestDtoAns();
        description.setDescription("item request");
        description.setId(1);
    }

    @Test
    void shouldAddItem() {
        assertThat(itemDto, equalTo(itemService.addItem(itemDto, 1)));
        itemDto.setRequestId(1);
        when(itemRequestService.findRequestById(anyInt(), anyInt()))
                .thenReturn(description);
        assertThat(itemDto, equalTo(itemService.addItem(itemDto, 1)));
    }

    @Test
    void shouldUpdateItem() {
        when(userService.findUserById(1))
                .thenReturn(user);
        when(itemRepository.findById(any()))
                .thenReturn(Optional.of(item));
        when(itemRequestService.findRequestById(anyInt(), anyInt()))
                .thenReturn(description);
        try {
            itemService.updateItem(itemDto, 3, 1);
        } catch (NonexistentException e) {
            assertThat(e.getMessage(), equalTo("Обновлять информацию о вещи может только её владелец"));
        }
        assertThat(itemDto, equalTo(itemService.updateItem(itemDto, 1, 1)));
        itemDto.setRequestId(1);
        assertThat(itemDto, equalTo(itemService.updateItem(itemDto, 1, 1)));
        ItemDto newItemDto = new ItemDto();
        newItemDto.setId(1);
        newItemDto.setName(null);
        newItemDto.setDescription(null);
        newItemDto.setAvailable(null);
        assertThat(itemDto, equalTo(itemService.updateItem(newItemDto, 1, 1)));
    }

    @Test
    void shouldAddComment() {
        Item item2 = new Item();
        item2.setId(2);
        item2.setName("item 2");
        item2.setDescription("item 2 description");
        item2.setAvailable(true);
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setEnd(LocalDateTime.MIN);
        Booking booking2 = new Booking();
        booking2.setItem(item2);
        booking2.setStatus(BookingStatus.APPROVED);
        booking2.setEnd(LocalDateTime.MIN);
        Booking booking3 = new Booking();
        booking3.setItem(item);
        booking3.setStatus(BookingStatus.REJECTED);
        booking3.setEnd(LocalDateTime.MIN);
        Booking booking4 = new Booking();
        booking4.setItem(item);
        booking4.setStatus(BookingStatus.APPROVED);
        booking4.setEnd(LocalDateTime.MAX);
        when(bookingDbRepository.findByBooker_IdOrderByStartDesc(user.getId()))
                .thenReturn(List.of(booking, booking2, booking3, booking4));
        when(bookingDbRepository.findByItem_IdOrderByStartDesc(item.getId()))
                .thenReturn(List.of(booking, booking2, booking3, booking4));
        when(bookingDbRepository.findByBooker_IdOrderByStartDesc(2))
                .thenReturn(new ArrayList<>());
        when(bookingDbRepository.findByBooker_IdOrderByStartDesc(3))
                .thenReturn(List.of(booking2, booking3, booking4));
        when(bookingDbRepository.findByItem_IdOrderByStartDesc(item2.getId()))
                .thenReturn(new ArrayList<>());
        when(itemRepository.findById(any()))
                .thenReturn(Optional.of(item));
        when(userService.findUserById(anyInt()))
                .thenReturn(user);
        RequestCommentDto request = new RequestCommentDto();
        request.setId(1);
        request.setText("text");
        try {
            itemService.addComment(request, item.getId(), 2);
        } catch (NullPointerException e) {
            assertThat(e.getMessage(), equalTo("Оставить комментарий можно только для той вещи, которую вы бронировали"));
        }
        try {
            itemService.addComment(request, item2.getId(), 1);
        } catch (NullPointerException e) {
            assertThat(e.getMessage(), equalTo("Оставить комментарий можно только для той вещи, которую вы бронировали"));
        }
        try {
            itemService.addComment(request, item.getId(), 3);
        } catch (NullPointerException e) {
            assertThat(e.getMessage(), equalTo("Оставить комментарий можно только для той вещи, которую вы бронировали"));
        }
        AnswerCommentDto answerCommentDto = itemService.addComment(request, 1, 1);
        assertThat(request.getId(), equalTo(answerCommentDto.getId()));
        assertThat(request.getText(), equalTo(answerCommentDto.getText()));
        assertThat(user.getName(), equalTo(answerCommentDto.getAuthorName()));
    }
}
