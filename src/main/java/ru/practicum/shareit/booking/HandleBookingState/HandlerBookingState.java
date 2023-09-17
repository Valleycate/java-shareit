package ru.practicum.shareit.booking.HandleBookingState;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.dao.BookingDbRepository;
import ru.practicum.shareit.booking.dto.BookingDtoAnswer;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exceptions.BadBookingState;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public abstract class HandlerBookingState {
    public final BookingDbRepository repository;
    public final BookingMapper mapper;
    public HandlerBookingState next;

    public static HandlerBookingState link(HandlerBookingState handler, HandlerBookingState... handlerNext) {
        HandlerBookingState head = handler;
        for (HandlerBookingState item : handlerNext) {
            head.next = item;
            head = item;
        }
        return handler;
    }

    public abstract String getState();

    public abstract List<BookingDtoAnswer> findBookings(int userId, int page    , int size);

    public List<BookingDtoAnswer> handle(int userId, String state, int page, int size) {
        if (state.equals(getState())) {
            return findBookings(userId, page, size);
        }
        if (getState().equals("Unknown state")) {
            throw new BadBookingState(state);
        }
        if (next == null) {
            return new ArrayList<>();
        }
        return next.handle(userId, state, page, size);
    }
}
