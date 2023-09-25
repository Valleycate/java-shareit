package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDtoAnswer;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.item.controller.XHeaderUserId.X_SHARER_USER_ID;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;

    @PostMapping()
    public BookingDtoAnswer addBooking(@Valid @RequestBody BookingDtoRequest bookingDto, @RequestHeader(X_SHARER_USER_ID) Integer id) {
        return service.addBooking(bookingDto, id);
    }

    @PatchMapping("/{bookingId}")
    BookingDtoAnswer approvedBooking(@RequestHeader(X_SHARER_USER_ID) Integer userId, @PathVariable Integer bookingId,
                                     @RequestParam Boolean approved) {
        return service.approvedBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoAnswer getBookingById(@PathVariable Integer bookingId, @RequestHeader(X_SHARER_USER_ID) Integer userId) {
        return service.getBookingById(bookingId, userId);
    }

    @GetMapping()
    public List<BookingDtoAnswer> getAllBookingByState(@RequestHeader(X_SHARER_USER_ID) Integer userId,
                                                       @RequestParam(defaultValue = "ALL") String state,
                                                       @RequestParam(defaultValue = "0") int from,
                                                       @RequestParam(defaultValue = "10") int size) {
        return service.getAllBookingByState(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDtoAnswer> getAllBookingByOwnerItemsAndState(@RequestHeader(X_SHARER_USER_ID) Integer userId,
                                                                    @RequestParam(defaultValue = "ALL") String state,
                                                                    @RequestParam(defaultValue = "0") int from,
                                                                    @RequestParam(defaultValue = "10") int size) {
        return service.getAllBookingByOwnerItemsAndState(userId, state, from, size);
    }
}
