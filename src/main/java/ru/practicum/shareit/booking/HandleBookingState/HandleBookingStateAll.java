package ru.practicum.shareit.booking.HandleBookingState;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.dao.BookingDbRepository;
import ru.practicum.shareit.booking.dto.BookingDtoAnswer;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.DtoState;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class HandleBookingStateAll implements HandlerBookingState {
    private final BookingDbRepository repository;
    private final BookingMapper mapper = Mappers.getMapper(BookingMapper.class);
    private HandlerBookingState handler;

    public void setNext(HandlerBookingState handler) {
        this.handler = handler;
    }

    public List<BookingDtoAnswer> handleState(Integer userId, String state) {
        if (state.equals(DtoState.ALL.toString())) {
            return repository.findByBooker_IdOrderByStartDesc(userId).stream()
                    .map(mapper::toBookingDto)
                    .collect(Collectors.toList());
        } else {
            return handler.handleState(userId, state);
        }
    }
}
