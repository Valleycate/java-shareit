package ru.practicum.shareit.booking.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingDbRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByBooker_IdOrderByStartDesc(Integer bookerId, Pageable pageable);

    List<Booking> findByBooker_IdOrderByStartDesc(Integer bookerId);

    List<Booking> findByBooker_IdAndStatusOrderByStartDesc(Integer bookerId, BookingStatus status, Pageable pageable);

    List<Booking> findByBooker_IdAndEndIsBeforeOrderByStartDesc(Integer bookerId, LocalDateTime end, Pageable pageable);

    List<Booking> findByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Integer bookerId, LocalDateTime start,
                                                                               LocalDateTime end, Pageable pageable);

    List<Booking> findByBooker_IdAndStartIsAfterOrderByStartDesc(Integer bookerId, LocalDateTime start, Pageable pageable);

    List<Booking> findByItem_IdOrderByStartDesc(int itemId, Pageable pageable);

    List<Booking> findByItem_IdOrderByStartDesc(int itemId);

    List<Booking> findByItem_IdAndStatusOrderByStartDesc(int itemId, BookingStatus status, Pageable pageable);

    List<Booking> findByItem_IdAndEndIsBeforeOrderByStartDesc(int itemId, LocalDateTime end, Pageable pageable);

    List<Booking> findByItem_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(int itemId, LocalDateTime start,
                                                                             LocalDateTime end, Pageable pageable);

    List<Booking> findByItem_IdAndStartIsAfterOrderByStartDesc(int itemId, LocalDateTime start, Pageable pageable);
}
