package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
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
    private BookingDbRepository repository;
    @Autowired
    private UserDbRepository userRepository;
    @Autowired
    private ItemDbRepository itemRepository;
    private Item item = new Item();
    private User user = new User();
    private Item item2 = new Item();

    @BeforeEach
    void beforeEach() {
        user.setName("user");
        user.setEmail("user@email");
        user = userRepository.save(user);
        item.setName("item 1");
        item.setDescription("item 1 description");
        item.setOwner(user);
        item.setAvailable(false);
        item = itemRepository.save(item);
        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setStart(LocalDateTime.of(2024, 12, 13, 3, 42));
        booking.setEnd(LocalDateTime.of(2025, 12, 13, 3, 42));
        booking.setItem(item);
        booking.setId(3);
        booking.setStatus(BookingStatus.WAITING);
        repository.save(booking);
        item2.setName("item 2");
        item2.setDescription("item 2 description");
        item2.setOwner(user);
        item2.setAvailable(true);
        item2 = itemRepository.save(item2);
        Booking booking2 = new Booking();
        booking2.setBooker(user);
        booking2.setStart(LocalDateTime.of(2024, 10, 1, 5, 0));
        booking2.setEnd(LocalDateTime.of(2025, 10, 1, 5, 0));
        booking2.setItem(item2);
        booking2.setStatus(BookingStatus.APPROVED);
        repository.save(booking2);
    }

    @Test
    public void shouldReturnAllBookingsByItem() {
        List<Booking> bookings = repository.findByItem_IdOrderByStartDesc(item.getId(), Pageable.ofSize(20));
        assertEquals(1, bookings.size());
    }

    @Test
    public void shouldReturnAllBookingsByItemIdAndEnd() {
        List<Booking> bookings = repository.findByItem_IdAndEndIsBeforeOrderByStartDesc(item2.getId(), LocalDateTime.MIN,
                Pageable.ofSize(20));
        assertEquals(1, bookings.size());
    }

    @Test
    public void shouldReturnAllBookingsByItemIdAndStart() {
        List<Booking> bookings = repository.findByItem_IdAndStartIsAfterOrderByStartDesc(item2.getId(), LocalDateTime.MAX,
                Pageable.ofSize(20));
        assertEquals(1, bookings.size());
    }

    @Test
    public void shouldReturnAllBookingsByItemWithoutPage() {
        List<Booking> bookings = repository.findByItem_IdOrderByStartDesc(item.getId());
        assertEquals(1, bookings.size());
    }

    @Test
    public void shouldReturnAllBookingsByItemIdAndStatus() {
        List<Booking> bookings = repository.findByItem_IdAndStatusOrderByStartDesc(item2.getId(), BookingStatus.APPROVED,
                Pageable.ofSize(20));
        assertEquals(1, bookings.size());
    }

    @Test
    public void shouldReturnAllBookingsByItemIdAndStartAndEnd() {
        List<Booking> bookings = repository.findByItem_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(item.getId(),
                LocalDateTime.MIN, LocalDateTime.MAX, Pageable.ofSize(20));
        assertEquals(1, bookings.size());
    }

    @Test
    public void shouldReturnAllBookingsByBookerId() {
        List<Booking> bookings = repository.findByBooker_IdOrderByStartDesc(user.getId(), Pageable.ofSize(20));
        assertEquals(2, bookings.size());
    }

    @Test
    public void shouldReturnAllBookingsByBookerIdAndStatus() {
        List<Booking> bookings = repository.findByBooker_IdAndStatusOrderByStartDesc(user.getId(), BookingStatus.WAITING,
                Pageable.ofSize(20));
        assertEquals(1, bookings.size());
    }

    @Test
    public void shouldReturnAllBookingsByBookerIdWithoutPage() {
        List<Booking> bookings = repository.findByBooker_IdOrderByStartDesc(user.getId());
        assertEquals(2, bookings.size());
    }

    @Test
    public void shouldReturnAllBookingsByBookerIdAndEnd() {
        List<Booking> bookings = repository.findByBooker_IdAndEndIsBeforeOrderByStartDesc(user.getId(), LocalDateTime.MIN,
                Pageable.ofSize(20));
        assertEquals(2, bookings.size());
    }

    @Test
    public void shouldReturnAllBookingsByBookerIdAndStart() {
        List<Booking> bookings = repository.findByBooker_IdAndStartIsAfterOrderByStartDesc(user.getId(), LocalDateTime.MAX,
                Pageable.ofSize(20));
        assertEquals(2, bookings.size());
    }

    @Test
    public void shouldReturnAllBookingsByStartAndEnd() {
        List<Booking> bookings = repository.findByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(user.getId(),
                LocalDateTime.MIN, LocalDateTime.MAX, Pageable.ofSize(20));
        assertEquals(2, bookings.size());
    }
}
