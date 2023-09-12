package ru.practicum.shareit.booking.HandleBookingState;

import ru.practicum.shareit.booking.dao.BookingDbRepository;
import ru.practicum.shareit.booking.dto.BookingDtoAnswer;
import ru.practicum.shareit.booking.dto.BookingMapper;

import java.util.List;


public class HandleBookingStateUnknown extends HandlerBookingState {

    public HandleBookingStateUnknown(BookingDbRepository repository, BookingMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public String getState() {
        return "Unknown state";
    }

    @Override
    public List<BookingDtoAnswer> findBookings(int userId) {
        return null;
    }
}
