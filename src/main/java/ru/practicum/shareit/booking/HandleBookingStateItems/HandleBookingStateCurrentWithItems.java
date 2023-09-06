package ru.practicum.shareit.booking.HandleBookingStateItems;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.dao.BookingDbRepository;
import ru.practicum.shareit.booking.dto.DtoState;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class HandleBookingStateCurrentWithItems implements HandlerBookingState {
    private final BookingDbRepository repository;
    private HandlerBookingState handler;

    public void setNext(HandlerBookingState handler) {
        this.handler = handler;
    }

    public List<Booking> handleState(Integer userId, String state, ItemServiceImpl itemService, List<Booking> ans) {
        if (state.equals(DtoState.CURRENT.toString())) {
            itemService.findAllItemsByUserForBooking(userId)
                    .forEach(itemDto -> ans.addAll(repository.findByItem_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                            itemDto.getId(), LocalDateTime.now(), LocalDateTime.now())));
            return ans;
        } else {
            return handler.handleState(userId, state, itemService, ans);
        }
    }
}

