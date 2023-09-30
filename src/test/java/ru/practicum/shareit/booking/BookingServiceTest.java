package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.booking.dao.BookingDbRepository;
import ru.practicum.shareit.booking.dto.BookingDtoAnswer;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exceptions.NonexistentException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class BookingServiceTest {
    private final BookingDbRepository repository = Mockito.mock(BookingDbRepository.class);
    private final UserService userService = Mockito.mock(UserService.class);
    private final ItemServiceImpl itemService = Mockito.mock(ItemServiceImpl.class);
    private final BookingService bookingService = new BookingServiceImpl(repository, userService, itemService);
    private User user;
    private User user2;
    private Item item;

    @BeforeEach()
    void beforeEach() {
        user = new User();
        user.setId(1);
        user.setName("user");
        user.setEmail("user@email");
        user2 = new User();
        user2.setId(2);
        user2.setName("user2");
        user2.setEmail("user2@email");
        item = new Item();
        item.setId(1);
        item.setName("item 1");
        item.setDescription("item 1 description");
        item.setOwner(user);
        item.setAvailable(false);
    }

    @Test
    void shouldAddBooking() {
        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest();
        bookingDtoRequest.setEnd(LocalDateTime.of(2024, 12, 13, 3, 42, 05));
        bookingDtoRequest.setStart(bookingDtoRequest.getEnd());
        try {
            bookingService.addBooking(bookingDtoRequest, 1);
        } catch (ValidationException e) {
            assertThat(e.getMessage(), equalTo("конец бронирования должен быть после начала бронирования"));
        }
        bookingDtoRequest.setEnd(LocalDateTime.of(2023, 12, 13, 3, 42, 05));
        try {
            bookingService.addBooking(bookingDtoRequest, 1);
        } catch (ValidationException e) {
            assertThat(e.getMessage(), equalTo("конец бронирования должен быть после начала бронирования"));
        }
        bookingDtoRequest.setEnd(LocalDateTime.MAX);
        bookingDtoRequest.setStart(LocalDateTime.now());
        bookingDtoRequest.setItemId(1);
        when(itemService.findItemById(anyInt()))
                .thenReturn(item);
        when(userService.findUserById(1))
                .thenReturn(user);
        when(userService.findUserById(2))
                .thenReturn(user2);
        try {
            bookingService.addBooking(bookingDtoRequest, 2);
        } catch (NullPointerException e) {
            assertThat(e.getMessage(), equalTo("вещь недоступна для бронирования"));
        }
        item.setAvailable(true);
        try {
            bookingService.addBooking(bookingDtoRequest, 1);
        } catch (NonexistentException e) {
            assertThat(e.getMessage(), equalTo("Нельзя брать в аренду свои вещи"));
        }
        BookingDtoAnswer bookingDtoAnswer = new BookingDtoAnswer();
        bookingDtoAnswer.setStart(bookingDtoRequest.getStart());
        bookingDtoAnswer.setEnd(bookingDtoRequest.getEnd());
        bookingDtoAnswer.setStatus(BookingStatus.WAITING);
        bookingDtoAnswer.setItem(item);
        bookingDtoAnswer.setBooker(user2);
        bookingDtoAnswer.setId(0);
        assertThat(bookingDtoAnswer, equalTo(bookingService.addBooking(bookingDtoRequest, 2)));
    }

    @Test
    void shouldApprovedBooking() {
        Booking booking = new Booking();
        booking.setBooker(user2);
        booking.setEnd(LocalDateTime.MAX);
        booking.setStart(LocalDateTime.now());
        booking.setItem(item);
        booking.setId(1);
        booking.setStatus(BookingStatus.WAITING);
        when(itemService.findItemById(anyInt()))
                .thenReturn(item);
        when(userService.findUserById(1))
                .thenReturn(user);
        when(userService.findUserById(2))
                .thenReturn(user2);
        BookingDtoAnswer bookingDtoAnswer = new BookingDtoAnswer();
        bookingDtoAnswer.setStart(booking.getStart());
        bookingDtoAnswer.setEnd(booking.getEnd());
        bookingDtoAnswer.setStatus(BookingStatus.APPROVED);
        bookingDtoAnswer.setItem(item);
        bookingDtoAnswer.setBooker(user2);
        bookingDtoAnswer.setId(1);
        try {
            bookingService.approvedBooking(user.getId(), booking.getId(), true);
        } catch (NonexistentException e) {
            assertThat(e.getMessage(), equalTo("Такой аренды нет"));
        }
        when(repository.findById(anyInt()))
                .thenReturn(Optional.of(booking));
        try {
            bookingService.approvedBooking(user2.getId(), booking.getId(), true);
        } catch (NonexistentException e) {
            assertThat(e.getMessage(), equalTo("Обновлять статус бронирования может только владелец вещи"));
        }
        assertThat(bookingDtoAnswer, equalTo(bookingService.approvedBooking(user.getId(),
                booking.getId(), true)));
        try {
            bookingService.approvedBooking(user.getId(), booking.getId(), false);
        } catch (ValidationException e) {
            assertThat(e.getMessage(), equalTo("Нельзя менять статус после как он уже был установлен"));
        }
        booking.setStatus(BookingStatus.WAITING);
        bookingDtoAnswer.setStatus(BookingStatus.REJECTED);
        assertThat(bookingDtoAnswer, equalTo(bookingService.approvedBooking(user.getId(),
                booking.getId(), false)));
        try {
            bookingService.approvedBooking(user.getId(), booking.getId(), true);
        } catch (ValidationException e) {
            assertThat(e.getMessage(), equalTo("Нельзя менять статус после как он уже был установлен"));
        }
    }
}
