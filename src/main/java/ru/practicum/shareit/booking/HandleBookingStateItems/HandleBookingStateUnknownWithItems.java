package ru.practicum.shareit.booking.HandleBookingStateItems;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.BadBookingState;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.util.List;

@RequiredArgsConstructor
public class HandleBookingStateUnknownWithItems implements HandlerBookingState {

    public void setNext(HandlerBookingState handler) {
    }

    public List<Booking> handleState(Integer userId, String state, ItemServiceImpl itemService, List<Booking> ans) {
        throw new BadBookingState("Unknown state: " + state);
    }
}
