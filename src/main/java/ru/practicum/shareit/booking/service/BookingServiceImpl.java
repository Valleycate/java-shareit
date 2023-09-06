package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.HandleBookingState.HandleBookingStateAll;
import ru.practicum.shareit.booking.HandleBookingState.HandleBookingStateCurrent;
import ru.practicum.shareit.booking.HandleBookingState.HandleBookingStateFuture;
import ru.practicum.shareit.booking.HandleBookingState.HandleBookingStatePast;
import ru.practicum.shareit.booking.HandleBookingState.HandleBookingStateRejected;
import ru.practicum.shareit.booking.HandleBookingState.HandleBookingStateUnknown;
import ru.practicum.shareit.booking.HandleBookingState.HandleBookingStateWaiting;
import ru.practicum.shareit.booking.HandleBookingStateItems.HandleBookingStateAllWithItems;
import ru.practicum.shareit.booking.HandleBookingStateItems.HandleBookingStateCurrentWithItems;
import ru.practicum.shareit.booking.HandleBookingStateItems.HandleBookingStateFutureWithItems;
import ru.practicum.shareit.booking.HandleBookingStateItems.HandleBookingStatePastWithItems;
import ru.practicum.shareit.booking.HandleBookingStateItems.HandleBookingStateRejectedWithItems;
import ru.practicum.shareit.booking.HandleBookingStateItems.HandleBookingStateUnknownWithItems;
import ru.practicum.shareit.booking.HandleBookingStateItems.HandleBookingStateWaitingWithItems;
import ru.practicum.shareit.booking.dao.BookingDbRepository;
import ru.practicum.shareit.booking.dto.BookingDtoAnswer;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.NonexistentException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingDbRepository repository;
    private final BookingMapper mapper = Mappers.getMapper(BookingMapper.class);
    private final UserService userService;
    private final ItemServiceImpl itemService;

    public BookingDtoAnswer addBooking(@Valid BookingDtoRequest bookingDto, Integer userId) {
        if (bookingDto.getEnd().isBefore(bookingDto.getStart()) || bookingDto.getStart().isEqual(bookingDto.getEnd())) {
            throw new ValidationException("конец бронирования должен быть после начала бронирования");
        }
        Item item = itemService.findItemById(bookingDto.getItemId());
        if (!item.getAvailable()) {
            throw new NullPointerException("вещь недоступна для бронирования");
        }
        if (item.getOwner().getId() == userId) {
            throw new NonexistentException("Нельзя брать в аренду свои вещи");
        }
        Booking booking = mapper.toBookingWithCheck(bookingDto, userService.findUserById(userId),
                itemService.findItemById(bookingDto.getItemId()), BookingStatus.WAITING);
        repository.save(booking);
        return mapper.toBookingDto(booking);
    }

    public BookingDtoAnswer approvedBooking(Integer userId, Integer bookingId, Boolean approved) {
        Optional<Booking> optionalBooking = repository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            throw new NonexistentException("Такой аренды нет");
        }
        Booking booking = optionalBooking.get();
        if (booking.getItem().getOwner().getId() != userId) {
            throw new NonexistentException("Обновлять статус бронирования может только владелец вещи");
        }
        if (booking.getStatus() == BookingStatus.APPROVED || booking.getStatus() == BookingStatus.REJECTED) {
            throw new ValidationException("Нельзя менять статус после как он уже был установлен");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        repository.save(booking);
        return mapper.toBookingDto(booking);
    }

    public BookingDtoAnswer getBookingById(Integer bookingId, Integer userId) {
        Optional<Booking> optionalBooking = repository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            throw new NonexistentException("Такой аренды нет");
        }
        Booking booking = optionalBooking.get();
        if (booking.getItem().getOwner().getId() != userId && booking.getBooker().getId() != userId) {
            throw new NonexistentException("Получение данных о конкретном бронировании может быть выполнено либо " +
                    "автором бронирования, либо владельцем вещи");
        }
        return mapper.toBookingDto(booking);
    }

    public List<BookingDtoAnswer> getAllBookingByState(Integer userId, String state) {
        HandleBookingStateAll handleAll = new HandleBookingStateAll(repository);
        HandleBookingStateFuture handleFuture = new HandleBookingStateFuture(repository);
        HandleBookingStatePast handlePast = new HandleBookingStatePast(repository);
        HandleBookingStateWaiting handleWaiting = new HandleBookingStateWaiting(repository);
        HandleBookingStateRejected handleRejected = new HandleBookingStateRejected(repository);
        HandleBookingStateCurrent handleCurrent = new HandleBookingStateCurrent(repository);
        HandleBookingStateUnknown handleUnknown = new HandleBookingStateUnknown();
        handleAll.setNext(handleFuture);
        handleFuture.setNext(handlePast);
        handlePast.setNext(handleWaiting);
        handleWaiting.setNext(handleRejected);
        handleRejected.setNext(handleCurrent);
        handleCurrent.setNext(handleUnknown);
        userService.findUserById(userId);
        return handleAll.handleState(userId, state);
    }

    public List<BookingDtoAnswer> getAllBookingByOwnerItemsAndState(Integer userId, String state) {
        HandleBookingStateAllWithItems handleAll = new HandleBookingStateAllWithItems(repository);
        HandleBookingStateFutureWithItems handleFuture = new HandleBookingStateFutureWithItems(repository);
        HandleBookingStatePastWithItems handlePast = new HandleBookingStatePastWithItems(repository);
        HandleBookingStateWaitingWithItems handleWaiting = new HandleBookingStateWaitingWithItems(repository);
        HandleBookingStateRejectedWithItems handleRejected = new HandleBookingStateRejectedWithItems(repository);
        HandleBookingStateCurrentWithItems handleCurrent = new HandleBookingStateCurrentWithItems(repository);
        HandleBookingStateUnknownWithItems handleUnknown = new HandleBookingStateUnknownWithItems();
        handleAll.setNext(handleFuture);
        handleFuture.setNext(handlePast);
        handlePast.setNext(handleWaiting);
        handleWaiting.setNext(handleRejected);
        handleRejected.setNext(handleCurrent);
        handleCurrent.setNext(handleUnknown);
        List<Booking> ans = new ArrayList<>(handleAll.handleState(userId, state, itemService, new ArrayList<>()));
        if (ans.isEmpty()) {
            throw new NonexistentException("У этого пользователя нет вещей");
        }
        return ans.stream()
                .map(mapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
