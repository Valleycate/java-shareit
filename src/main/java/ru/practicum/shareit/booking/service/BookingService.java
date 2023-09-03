package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoAnswer;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;

import java.util.List;

public interface BookingService {
    BookingDtoAnswer addBooking(BookingDtoRequest bookingDto, Integer userId);

    BookingDtoAnswer approvedBooking(Integer userId, Integer bookingId, Boolean approved);

    BookingDtoAnswer getBookingById(Integer bookingId, Integer userId);

    List<BookingDtoAnswer> getAllBookingByState(Integer userId, String state);

    List<BookingDtoAnswer> getAllBookingByOwnerItemsAndState(Integer userId, String state);
}
