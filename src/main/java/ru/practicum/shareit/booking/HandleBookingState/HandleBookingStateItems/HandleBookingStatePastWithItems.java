package ru.practicum.shareit.booking.HandleBookingState.HandleBookingStateItems;

import ru.practicum.shareit.booking.HandleBookingState.HandlerBookingState;
import ru.practicum.shareit.booking.dao.BookingDbRepository;
import ru.practicum.shareit.booking.dto.BookingDtoAnswer;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.DtoState;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HandleBookingStatePastWithItems extends HandlerBookingState {
    private final ItemServiceImpl itemService;

    public HandleBookingStatePastWithItems(BookingDbRepository repository, BookingMapper mapper, ItemServiceImpl itemService) {
        super(repository, mapper);
        this.itemService = itemService;
    }

    @Override
    public String getState() {
        return DtoState.PAST.toString();
    }

    @Override
    public List<BookingDtoAnswer> findBookings(int userId) {
        List<BookingDtoAnswer> ans = new ArrayList<>();
        itemService.findAllItemsByUserForBooking(userId)
                .forEach(itemDto -> ans.addAll(repository.findByItem_IdAndEndIsBeforeOrderByStartDesc(
                                itemDto.getId(), LocalDateTime.now()).stream()
                        .map(mapper::toBookingDto)
                        .collect(Collectors.toList())));
        return ans;
    }
}
