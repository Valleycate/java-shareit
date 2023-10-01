package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import ru.practicum.shareit.booking.HandleBookingState.HandleBookingStateAll;
import ru.practicum.shareit.booking.HandleBookingState.HandleBookingStateItems.HandleBookingStateAllWithItems;
import ru.practicum.shareit.booking.HandleBookingState.HandleBookingStateItems.HandleBookingStateCurrentWithItems;
import ru.practicum.shareit.booking.HandleBookingState.HandleBookingStateItems.HandleBookingStateFutureWithItems;
import ru.practicum.shareit.booking.HandleBookingState.HandleBookingStateItems.HandleBookingStatePastWithItems;
import ru.practicum.shareit.booking.HandleBookingState.HandleBookingStateItems.HandleBookingStateRejectedWithItems;
import ru.practicum.shareit.booking.HandleBookingState.HandleBookingStateItems.HandleBookingStateWaitingWithItems;
import ru.practicum.shareit.booking.HandleBookingState.HandleBookingStateUnknown;
import ru.practicum.shareit.booking.HandleBookingState.HandlerBookingState;
import ru.practicum.shareit.booking.dao.BookingDbRepository;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.DtoState;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.BadBookingState;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class HandlerBookingStateWithItemsTest {
    private final BookingDbRepository repository = Mockito.mock(BookingDbRepository.class);
    private final ItemServiceImpl itemService = Mockito.mock(ItemServiceImpl.class);
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
        ItemDto itemDto = new ItemDto();
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setId(item.getId());
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
        when(itemService.findAllItemsByUserForBooking(anyInt()))
                .thenReturn(List.of(itemDto));
        when(repository.findByItem_IdOrderByStartDesc(anyInt(), any()))
                .thenReturn(List.of(booking));
        when(repository.findByItem_IdAndStatusOrderByStartDesc(anyInt(), any(), any()))
                .thenReturn(List.of(booking));
        when(repository.findByItem_IdAndEndIsBeforeOrderByStartDesc(anyInt(), any(), any()))
                .thenReturn(List.of(booking));
        when(repository.findByItem_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(anyInt(), any(), any(), any()))
                .thenReturn(List.of(booking));
        when(repository.findByItem_IdAndStartIsAfterOrderByStartDesc(anyInt(), any(), any()))
                .thenReturn(List.of(booking));
        HandlerBookingState handlerAll = HandlerBookingState.link(new HandleBookingStateAll(repository, mapper));
        HandlerBookingState handlers = HandlerBookingState.link(
                new HandleBookingStateAllWithItems(repository, mapper, itemService),
                new HandleBookingStateFutureWithItems(repository, mapper, itemService),
                new HandleBookingStatePastWithItems(repository, mapper, itemService),
                new HandleBookingStateWaitingWithItems(repository, mapper, itemService),
                new HandleBookingStateRejectedWithItems(repository, mapper, itemService),
                new HandleBookingStateCurrentWithItems(repository, mapper, itemService),
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
