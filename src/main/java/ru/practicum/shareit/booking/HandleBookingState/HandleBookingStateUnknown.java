package ru.practicum.shareit.booking.HandleBookingState;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoAnswer;
import ru.practicum.shareit.exceptions.BadBookingState;

import java.util.List;

@RequiredArgsConstructor
public class HandleBookingStateUnknown implements HandlerBookingState {

    public void setNext(HandlerBookingState handler) {
    }

    public List<BookingDtoAnswer> handleState(Integer userId, String state) {
        throw new BadBookingState("Unknown state: " + state);
    }
}
