package ru.practicum.shareit.booking.HandleBookingState;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dao.BookingDbRepository;
import ru.practicum.shareit.booking.dto.BookingDtoAnswer;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.DtoState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class HandleBookingStatePast extends HandlerBookingState {
    public HandleBookingStatePast(BookingDbRepository repository, BookingMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public String getState() {
        return DtoState.PAST.toString();
    }

    @Override
    public List<BookingDtoAnswer> findBookings(int userId, int page, int size) {
        return repository.findByBooker_IdAndEndIsBeforeOrderByStartDesc(userId, LocalDateTime.now(),
                        PageRequest.of(page, size)).stream()
                .map(mapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
