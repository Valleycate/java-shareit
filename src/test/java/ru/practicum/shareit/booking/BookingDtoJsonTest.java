package ru.practicum.shareit.booking;

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
    @Autowired
    private JacksonTester<BookingDtoRequest> json;

    @Test
    void testBookingDto() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test");
        itemDto.setDescription("test description");
        itemDto.setAvailable(true);
        itemDto.setId(1);
        UserDto booker = new UserDto();
        booker.setName("test");
        booker.setEmail("test@email");
        booker.setId(2);
        BookingDtoRequest bookingRequest = new BookingDtoRequest();
        bookingRequest.setId(1);
        bookingRequest.setEnd(LocalDateTime.MAX);
        bookingRequest.setStart(LocalDateTime.of(2024, 12, 13, 3, 42, 05));
        bookingRequest.setItemId(itemDto.getId());
        JsonContent<BookingDtoRequest> result = json.write(bookingRequest);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(bookingRequest.getEnd().toString());
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(bookingRequest.getStart().toString());
    }

    @Test
    void mapperBookingDto() {
        User user = new User();
        user.setName("test");
        user.setEmail("test@email");
        user.setId(2);
        Item item = new Item();
        item.setName("test");
        item.setDescription("test description");
        item.setAvailable(true);
        item.setId(1);
        BookingDtoRequest bookingRequest = new BookingDtoRequest();
        bookingRequest.setItemId(item.getId());
        bookingRequest.setId(1);
        bookingRequest.setEnd(LocalDateTime.MAX);
        bookingRequest.setStart(LocalDateTime.of(2024, 12, 13, 3, 42, 05));
        BookingMapper mapper = Mappers.getMapper(BookingMapper.class);
        assertNull(mapper.toBookingDto(null));
        assertNull(mapper.toBookingWithCheck(null, null, null, null));
        assertNotNull(mapper.toBookingWithCheck(null, user, null, null));
        assertNotNull(mapper.toBookingWithCheck(null, user, item, null));
        assertNotNull(mapper.toBookingWithCheck(null, user, item, BookingStatus.WAITING));
        Booking booking = mapper.toBookingWithCheck(bookingRequest, user, item, BookingStatus.WAITING);
        assertEquals(user, booking.getBooker());
        assertEquals(item, booking.getItem());
        assertEquals(BookingStatus.WAITING, booking.getStatus());
        assertEquals(bookingRequest.getStart(), booking.getStart());
        assertEquals(bookingRequest.getEnd(), booking.getEnd());
        assertEquals(bookingRequest.getId(), booking.getId());
        assertEquals(bookingRequest.getItemId(), booking.getItem().getId());
        BookingDtoAnswer bookingDtoAnswer = mapper.toBookingDto(booking);
        assertEquals(BookingStatus.WAITING, bookingDtoAnswer.getStatus());
        assertEquals(bookingRequest.getStart(), bookingDtoAnswer.getStart());
        assertEquals(bookingRequest.getEnd(), bookingDtoAnswer.getEnd());
        assertEquals(bookingRequest.getId(), bookingDtoAnswer.getId());
        assertEquals(bookingRequest.getItemId(), bookingDtoAnswer.getItem().getId());
    }
}

