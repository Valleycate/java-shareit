package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDtoAnswer;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@JsonTest
public class BookingDtoJsonTest {
    private final BookingMapper mapper = Mappers.getMapper(BookingMapper.class);
    @Autowired
    private JacksonTester<BookingDtoRequest> json;
    private User user;
    private Item item;
    private BookingDtoRequest bookingRequest;
    private Booking booking;

    @BeforeEach()
    void beforeEach() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test");
        itemDto.setDescription("test description");
        itemDto.setAvailable(true);
        itemDto.setId(1);
        UserDto booker = new UserDto();
        booker.setName("test");
        booker.setEmail("test@email");
        booker.setId(2);
        user = new User();
        user.setName("test");
        user.setEmail("test@email");
        user.setId(2);
        item = new Item();
        item.setName("test");
        item.setDescription("test description");
        item.setAvailable(true);
        item.setId(1);
        bookingRequest = new BookingDtoRequest();
        bookingRequest.setId(1);
        bookingRequest.setEnd(LocalDateTime.MAX);
        bookingRequest.setStart(LocalDateTime.of(2024, 12, 13, 3, 42, 05));
        bookingRequest.setItemId(item.getId());
        booking = new Booking();
        booking.setId(1);
        booking.setEnd(LocalDateTime.MAX);
        booking.setStart(LocalDateTime.of(2024, 12, 13, 3, 42, 05));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
    }

    @Test
    void testBookingDto() throws Exception {
        JsonContent<BookingDtoRequest> result = json.write(bookingRequest);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(bookingRequest.getEnd().toString());
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(bookingRequest.getStart().toString());
    }

    @Test
    void shouldReturnNotNull() {
        assertNotNull(mapper.toBookingWithCheck(null, user, null, null));
        assertNotNull(mapper.toBookingWithCheck(null, user, item, null));
        assertNotNull(mapper.toBookingWithCheck(null, user, item, BookingStatus.WAITING));
    }

    @Test
    void shouldReturnNull() {
        assertNull(mapper.toBookingDto(null));
        assertNull(mapper.toBookingWithCheck(null, null, null, null));
    }

    @Test
    void shouldReturnBooking() {
        Booking booking = mapper.toBookingWithCheck(bookingRequest, user, item, BookingStatus.WAITING);
        assertEquals(user, booking.getBooker());
        assertEquals(item, booking.getItem());
        assertEquals(BookingStatus.WAITING, booking.getStatus());
        assertEquals(bookingRequest.getStart(), booking.getStart());
        assertEquals(bookingRequest.getEnd(), booking.getEnd());
        assertEquals(bookingRequest.getId(), booking.getId());
        assertEquals(bookingRequest.getItemId(), booking.getItem().getId());
    }

    @Test
    void shouldReturnBookingDto() {
        BookingDtoAnswer bookingDtoAnswer = mapper.toBookingDto(booking);
        assertEquals(BookingStatus.WAITING, bookingDtoAnswer.getStatus());
        assertEquals(booking.getStart(), bookingDtoAnswer.getStart());
        assertEquals(booking.getEnd(), bookingDtoAnswer.getEnd());
        assertEquals(booking.getId(), bookingDtoAnswer.getId());
        assertEquals(booking.getItem().getId(), bookingDtoAnswer.getItem().getId());
    }
}

