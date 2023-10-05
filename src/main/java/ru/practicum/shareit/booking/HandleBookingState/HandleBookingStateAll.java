package ru.practicum.shareit.booking.HandleBookingState;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dao.BookingDbRepository;
import ru.practicum.shareit.booking.dto.BookingDtoAnswer;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.DtoState;

import java.util.List;
import java.util.stream.Collectors;

public class HandleBookingStateAll extends HandlerBookingState {
    public HandleBookingStateAll(BookingDbRepository repository, BookingMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public String getState() {
        return DtoState.ALL.toString();
    }

    @Override
    public List<BookingDtoAnswer> findBookings(int userId, int page, int size) {
        return repository.findByBooker_IdOrderByStartDesc(userId, PageRequest.of(page, size)).stream()
                .map(mapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
