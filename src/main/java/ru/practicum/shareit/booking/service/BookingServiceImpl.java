package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingDbRepository;
import ru.practicum.shareit.booking.dto.BookingDtoAnswer;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.DtoState;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.BadBookingState;
import ru.practicum.shareit.exceptions.NonexistentException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.time.LocalDateTime;
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
        userService.findUserById(userId);
        if (state.equals(DtoState.ALL.toString())) {
            return repository.findByBooker_IdOrderByStartDesc(userId).stream()
                    .map(mapper::toBookingDto)
                    .collect(Collectors.toList());
        } else if (state.equals(DtoState.FUTURE.toString())) {
            return repository.findByBooker_IdAndStartIsAfterOrderByStartDesc(userId, LocalDateTime.now()).stream()
                    .map(mapper::toBookingDto)
                    .collect(Collectors.toList());
        } else if (state.equals(DtoState.PAST.toString())) {
            return repository.findByBooker_IdAndEndIsBeforeOrderByStartDesc(userId, LocalDateTime.now()).stream()
                    .map(mapper::toBookingDto)
                    .collect(Collectors.toList());
        } else if (state.equals(DtoState.WAITING.toString())) {
            return repository.findByBooker_IdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING).stream()
                    .map(mapper::toBookingDto)
                    .collect(Collectors.toList());
        } else if (state.equals(DtoState.REJECTED.toString())) {
            return repository.findByBooker_IdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED).stream()
                    .map(mapper::toBookingDto)
                    .collect(Collectors.toList());
        } else if (state.equals(DtoState.CURRENT.toString())) {
            return repository.findByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId, LocalDateTime.now(),
                            LocalDateTime.now()).stream()
                    .map(mapper::toBookingDto)
                    .collect(Collectors.toList());
        } else {
            throw new BadBookingState("Unknown state: " + state);
        }
    }

    public List<BookingDtoAnswer> getAllBookingByOwnerItemsAndState(Integer userId, String state) {
        List<Booking> ans = new ArrayList<>();
        if (state.equals(DtoState.ALL.toString())) {
            itemService.findAllItemsByUserForBooking(userId)
                    .forEach(itemDto -> ans.addAll(repository.findByItem_IdOrderByStartDesc(itemDto.getId())));
        } else if (state.equals(DtoState.FUTURE.toString())) {
            itemService.findAllItemsByUserForBooking(userId)
                    .forEach(itemDto -> ans.addAll(repository.findByItem_IdAndStartIsAfterOrderByStartDesc(
                            itemDto.getId(), LocalDateTime.now())));
        } else if (state.equals(DtoState.PAST.toString())) {
            itemService.findAllItemsByUserForBooking(userId)
                    .forEach(itemDto -> ans.addAll(repository.findByItem_IdAndEndIsBeforeOrderByStartDesc(
                            itemDto.getId(), LocalDateTime.now())));
        } else if (state.equals(DtoState.WAITING.toString())) {
            itemService.findAllItemsByUserForBooking(userId)
                    .forEach(itemDto -> ans.addAll(repository.findByItem_IdAndStatusOrderByStartDesc(itemDto.getId(),
                            BookingStatus.WAITING)));
        } else if (state.equals(DtoState.REJECTED.toString())) {
            itemService.findAllItemsByUserForBooking(userId)
                    .forEach(itemDto -> ans.addAll(repository.findByItem_IdAndStatusOrderByStartDesc(itemDto.getId(),
                            BookingStatus.REJECTED)));
        } else if (state.equals(DtoState.CURRENT.toString())) {
            itemService.findAllItemsByUserForBooking(userId)
                    .forEach(itemDto -> ans.addAll(repository.findByItem_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                            itemDto.getId(), LocalDateTime.now(), LocalDateTime.now())));
        } else {
            throw new BadBookingState("Unknown state: " + state);
        }
        if (ans.isEmpty()) {
            throw new NonexistentException("У этого пользователя нет вещей");
        }
        return ans.stream()
                .map(mapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
