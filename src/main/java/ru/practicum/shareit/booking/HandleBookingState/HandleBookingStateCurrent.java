package ru.practicum.shareit.booking.HandleBookingState;

import ru.practicum.shareit.booking.dao.BookingDbRepository;
import ru.practicum.shareit.booking.dto.BookingDtoAnswer;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.DtoState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class HandleBookingStateCurrent extends HandlerBookingState {

    public HandleBookingStateCurrent(BookingDbRepository repository, BookingMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public String getState() {
        return DtoState.CURRENT.toString();
    }

    @Override
    public List<BookingDtoAnswer> findBookings(int userId) {
        return repository.findByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId,
                        LocalDateTime.now(),
                        LocalDateTime.now()).stream()
                .map(mapper::toBookingDto)
                .collect(Collectors.toList());
    }
}

