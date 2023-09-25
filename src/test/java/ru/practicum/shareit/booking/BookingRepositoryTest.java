package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dao.BookingDbRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dao.ItemDbRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDbRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class BookingRepositoryTest {
    @Autowired
    BookingDbRepository repository;
    @Autowired
    UserDbRepository userRepository;
    @Autowired
    ItemDbRepository itemRepository;

    @Test
    void shouldReturnAllBookingsByItem() {
        User user = new User();
        user.setId(1);
        user.setName("user");
        user.setEmail("user@email");
        user = userRepository.save(user);
        Item item = new Item();
        item.setId(1);
        item.setName("item 1");
        item.setDescription("item 1 description");
        item.setOwner(user);
        item.setAvailable(true);
        item = itemRepository.save(item);
        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setEnd(LocalDateTime.MAX);
        booking.setStart(LocalDateTime.now());
        booking.setItem(item);
        booking.setId(1);
        booking.setStatus(BookingStatus.WAITING);
        repository.save(booking);
        List<Booking> bookings = repository.findByItem_IdOrderByStartDesc(item.getId(), Pageable.unpaged());
        assertEquals(1, bookings.size());
    }
}
