package ru.practicum.shareit.booking.HandleBookingStateItems;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.util.List;

public interface HandlerBookingState {
    void setNext(HandlerBookingState handler);

    List<Booking> handleState(Integer userId, String state, ItemServiceImpl itemService, List<Booking> ans);
}
