package ru.practicum.shareit.booking.HandleBookingState;

import ru.practicum.shareit.booking.dto.BookingDtoAnswer;

import java.util.List;

public interface HandlerBookingState {
    void setNext(HandlerBookingState handler);

    List<BookingDtoAnswer> handleState(Integer userId, String state);
}
