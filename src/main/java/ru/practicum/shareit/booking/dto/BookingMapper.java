package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;

@Mapper
public interface BookingMapper {
    @Mapping(target = "booker", source = "booker")
    @Mapping(target = "item", source = "item")
    @Mapping(target = "id", source = "bookingDto.id")
    Booking toBookingWithCheck(@Valid BookingDtoRequest bookingDto, User booker, Item item, BookingStatus status);

    BookingDtoAnswer toBookingDto(@Valid Booking booking);
}
