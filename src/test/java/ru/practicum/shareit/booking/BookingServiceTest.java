package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.booking.dao.BookingDbRepository;
import ru.practicum.shareit.booking.dto.BookingDtoAnswer;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
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
    BookingDbRepository repository = Mockito.mock(BookingDbRepository.class);
    UserService userService = Mockito.mock(UserService.class);
    ItemServiceImpl itemService = Mockito.mock(ItemServiceImpl.class);
    BookingService bookingService = new BookingServiceImpl(repository, userService, itemService);

    @Test
    void shouldAddBooking() {
        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest();
        bookingDtoRequest.setEnd(LocalDateTime.MAX);
        bookingDtoRequest.setStart(LocalDateTime.now());
        bookingDtoRequest.setItemId(1);
        User user = new User();
        user.setId(1);
        user.setName("user");
        user.setEmail("user@email");
        User user2 = new User();
        user2.setId(2);
        user2.setName("user2");
        user2.setEmail("user2@email");
        Item item = new Item();
        item.setId(1);
        item.setName("item 1");
        item.setDescription("item 1 description");
        item.setOwner(user);
        item.setAvailable(true);
        when(itemService.findItemById(anyInt()))
                .thenReturn(item);
        when(userService.findUserById(1))
                .thenReturn(user);
        when(userService.findUserById(2))
                .thenReturn(user2);
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
        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setEnd(LocalDateTime.MAX);
        booking.setStart(LocalDateTime.now());
        booking.setItem(item);
        booking.setId(1);
        booking.setStatus(BookingStatus.WAITING);
        when(itemService.findItemById(anyInt()))
                .thenReturn(item);
        when(userService.findUserById(2))
                .thenReturn(user);
        BookingDtoAnswer bookingDtoAnswer = new BookingDtoAnswer();
        bookingDtoAnswer.setStart(booking.getStart());
        bookingDtoAnswer.setEnd(booking.getEnd());
        bookingDtoAnswer.setStatus(BookingStatus.APPROVED);
        bookingDtoAnswer.setItem(item);
        bookingDtoAnswer.setBooker(user);
        bookingDtoAnswer.setId(1);
        when(repository.findById(anyInt()))
                .thenReturn(Optional.of(booking));
        assertThat(bookingDtoAnswer, equalTo(bookingService.approvedBooking(user.getId(),
                booking.getId(), true)));
    }
}
