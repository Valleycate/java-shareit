package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDtoAnswer;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.DtoState;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookingServiceImplTest {
    private final BookingServiceImpl bookingService;
    private final ItemServiceImpl itemService;
    private final UserServiceImpl userService;
    private final EntityManager em;

    @Test
    void shouldGetAllBookingByState() {
        UserDto itemOwner = new UserDto();
        itemOwner.setName("owner");
        itemOwner.setEmail("owner@email");
        itemOwner = userService.addUser(itemOwner);
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test");
        itemDto.setDescription("test description");
        itemDto.setAvailable(true);
        itemDto = itemService.addItem(itemDto, itemOwner.getId());
        UserDto booker = new UserDto();
        booker.setName("test");
        booker.setEmail("test@email");
        booker = userService.addUser(booker);
        BookingDtoRequest bookingRequest = new BookingDtoRequest();
        bookingRequest.setEnd(LocalDateTime.MAX);
        bookingRequest.setStart(LocalDateTime.of(2024, 12, 13, 3, 42, 05));
        bookingRequest.setItemId(itemDto.getId());
        bookingService.addBooking(bookingRequest, booker.getId());
        List<BookingDtoAnswer> allBooking = bookingService.getAllBookingByState(booker.getId(), DtoState.ALL.toString(), 0, 10);
        TypedQuery<Booking> query = em.createQuery("From booking", Booking.class);
        List<Booking> itemRequests = query.getResultList();
        assertThat(allBooking.size(), equalTo(itemRequests.size()));
    }
}
