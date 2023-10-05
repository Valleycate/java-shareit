package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import ru.practicum.shareit.booking.HandleBookingState.HandleBookingStateAll;
import ru.practicum.shareit.booking.HandleBookingState.HandleBookingStateCurrent;
import ru.practicum.shareit.booking.HandleBookingState.HandleBookingStateFuture;
import ru.practicum.shareit.booking.HandleBookingState.HandleBookingStatePast;
import ru.practicum.shareit.booking.HandleBookingState.HandleBookingStateRejected;
import ru.practicum.shareit.booking.HandleBookingState.HandleBookingStateUnknown;
import ru.practicum.shareit.booking.HandleBookingState.HandleBookingStateWaiting;
import ru.practicum.shareit.booking.HandleBookingState.HandlerBookingState;
import ru.practicum.shareit.booking.dao.BookingDbRepository;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.DtoState;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.BadBookingState;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;


public class HandlerBookingStateTest {
    private final BookingDbRepository repository = Mockito.mock(BookingDbRepository.class);
    private final BookingMapper mapper = Mappers.getMapper(BookingMapper.class);

    @Test
    void shouldHandleAllState() {
        User user = new User();
        user.setId(1);
        user.setName("user");
        user.setEmail("user@email");
        Item item = new Item();
        item.setId(1);
        item.setName("item 1");
        item.setDescription("item 1 description");
        item.setOwner(user);
        item.setAvailable(true);
        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setEnd(LocalDateTime.MAX);
        booking.setStart(LocalDateTime.now());
        booking.setItem(item);
        booking.setId(1);
        booking.setStatus(BookingStatus.WAITING);
        List<String> state = new ArrayList<>(List.of(DtoState.ALL.toString(), DtoState.FUTURE.toString(),
                DtoState.PAST.toString(), DtoState.WAITING.toString(), DtoState.REJECTED.toString(),
                DtoState.CURRENT.toString(), "Unknown status"));
        when(repository.findByBooker_IdOrderByStartDesc(anyInt(), any()))
                .thenReturn(List.of(booking));
        when(repository.findByBooker_IdAndStatusOrderByStartDesc(anyInt(), any(), any()))
                .thenReturn(List.of(booking));
        when(repository.findByBooker_IdAndEndIsBeforeOrderByStartDesc(anyInt(), any(), any()))
                .thenReturn(List.of(booking));
        when(repository.findByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(anyInt(), any(), any(), any()))
                .thenReturn(List.of(booking));
        when(repository.findByBooker_IdAndStartIsAfterOrderByStartDesc(anyInt(), any(), any()))
                .thenReturn(List.of(booking));
        HandlerBookingState handlerAll = HandlerBookingState.link(new HandleBookingStateAll(repository, mapper));
        HandlerBookingState handlers = HandlerBookingState.link(
                new HandleBookingStateAll(repository, mapper),
                new HandleBookingStateFuture(repository, mapper),
                new HandleBookingStatePast(repository, mapper),
                new HandleBookingStateWaiting(repository, mapper),
                new HandleBookingStateRejected(repository, mapper),
                new HandleBookingStateCurrent(repository, mapper),
                new HandleBookingStateUnknown(repository, mapper));
        String message = "";
        try {
            state.forEach(status -> handlers.handle(user.getId(), status, 0, 20));
        } catch (BadBookingState e) {
            message = e.getMessage();
        }
        assertEquals(message, "Unknown status");
        assertEquals(0, handlerAll.handle(user.getId(), "Unknown state", 0, 2).size());
    }
}
