package ru.practicum.shareit.booking.HandleBookingState.HandleBookingStateItems;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.HandleBookingState.HandlerBookingState;
import ru.practicum.shareit.booking.dao.BookingDbRepository;
import ru.practicum.shareit.booking.dto.BookingDtoAnswer;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.DtoState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class HandleBookingStateRejectedWithItems extends HandlerBookingState {
    private final ItemServiceImpl itemService;

    public HandleBookingStateRejectedWithItems(BookingDbRepository repository, BookingMapper mapper, ItemServiceImpl itemService) {
        super(repository, mapper);
        this.itemService = itemService;
    }

    @Override
    public String getState() {
        return DtoState.REJECTED.toString();
    }

    @Override
    public List<BookingDtoAnswer> findBookings(int userId, int page, int size) {
        List<BookingDtoAnswer> ans = new ArrayList<>();
        itemService.findAllItemsByUserForBooking(userId)
                .forEach(itemDto -> ans.addAll(repository.findByItem_IdAndStatusOrderByStartDesc(itemDto.getId(),
                                BookingStatus.REJECTED, PageRequest.of(page, size)).stream()
                        .map(mapper::toBookingDto)
                        .collect(Collectors.toList())));
        return ans;
    }
}

