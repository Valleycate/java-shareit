package ru.practicum.shareit.booking.HandleBookingState;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dao.BookingDbRepository;
import ru.practicum.shareit.booking.dto.BookingDtoAnswer;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.DtoState;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;
import java.util.stream.Collectors;

public class HandleBookingStateRejected extends HandlerBookingState {
    public HandleBookingStateRejected(BookingDbRepository repository, BookingMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public String getState() {
        return DtoState.REJECTED.toString();
    }

    @Override
    public List<BookingDtoAnswer> findBookings(int userId, int page, int size) {
        return repository.findByBooker_IdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED,
                        PageRequest.of(page, size)).stream()
                .map(mapper::toBookingDto)
                .collect(Collectors.toList());
    }
}

