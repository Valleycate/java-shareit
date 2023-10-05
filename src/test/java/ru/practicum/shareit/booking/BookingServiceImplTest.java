package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDtoAnswer;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.DtoState;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exceptions.NonexistentException;
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
    private UserDto itemOwner;
    private ItemDto itemDto;
    private UserDto booker;
    private BookingDtoRequest bookingRequest;

    @BeforeEach
    void beforeEach() {
        itemOwner = new UserDto();
        itemOwner.setName("owner");
        itemOwner.setEmail("owner@email");
        itemOwner = userService.addUser(itemOwner);
        itemDto = new ItemDto();
        itemDto.setName("test");
        itemDto.setDescription("test description");
        itemDto.setAvailable(true);
        itemDto = itemService.addItem(itemDto, itemOwner.getId());
        booker = new UserDto();
        booker.setName("test");
        booker.setEmail("test@email");
        booker = userService.addUser(booker);
        bookingRequest = new BookingDtoRequest();
        bookingRequest.setEnd(LocalDateTime.MAX);
        bookingRequest.setStart(LocalDateTime.of(2024, 12, 13, 3, 42, 05));
        bookingRequest.setItemId(itemDto.getId());
        bookingRequest.setId(bookingService.addBooking(bookingRequest, booker.getId()).getId());
    }

    @Test
    void shouldGetAllBookingByState() {
        List<BookingDtoAnswer> allBooking = bookingService.getAllBookingByState(booker.getId(), DtoState.ALL.toString(),
                0, 10);
        TypedQuery<Booking> query = em.createQuery("From booking", Booking.class);
        List<Booking> itemRequests = query.getResultList();
        assertThat(allBooking.size(), equalTo(itemRequests.size()));
        try {
            bookingService.getAllBookingByState(booker.getId(), DtoState.ALL.toString(), -1, 10);
        } catch (Exception e) {
            assertThat(e.getMessage(), equalTo("Page index must not be less than zero"));
        }
    }

    @Test
    void shouldGetBookingById() {
        try {
            bookingService.getBookingById(-1, 0);
        } catch (NonexistentException e) {
            assertThat(e.getMessage(), equalTo("Такой аренды нет"));
        }
        UserDto newUser = new UserDto();
        newUser.setName("newUser");
        newUser.setEmail("newUser@email");
        newUser = userService.addUser(newUser);
        try {
            bookingService.getBookingById(bookingRequest.getId(), newUser.getId());
        } catch (NonexistentException e) {
            assertThat(e.getMessage(), equalTo("Получение данных о конкретном бронировании может быть выполнено либо " +
                    "автором бронирования, либо владельцем вещи"));
        }
        BookingDtoAnswer bookingDtoAnswer = new BookingDtoAnswer();
        bookingDtoAnswer.setStart(bookingRequest.getStart());
        bookingDtoAnswer.setEnd(bookingRequest.getEnd());
        bookingDtoAnswer.setStatus(BookingStatus.WAITING);
        bookingDtoAnswer.setItem(itemService.findItemById(itemDto.getId()));
        bookingDtoAnswer.setBooker(userService.findUserById(booker.getId()));
        bookingDtoAnswer.setId(bookingRequest.getId());
        assertThat(bookingDtoAnswer, equalTo(bookingService.getBookingById(bookingRequest.getId(), booker.getId())));
        assertThat(bookingDtoAnswer, equalTo(bookingService.getBookingById(bookingRequest.getId(), itemOwner.getId())));
    }

    @Test
    void shouldGetAllBookingByOwnerItemsAndState() {
        try {
            bookingService.getAllBookingByOwnerItemsAndState(booker.getId(), DtoState.ALL.toString(), 0, 10);
        } catch (NonexistentException e) {
            assertThat(e.getMessage(), equalTo("У этого пользователя нет вещей"));
        }
        List<BookingDtoAnswer> allBooking = bookingService.getAllBookingByOwnerItemsAndState(itemOwner.getId(), DtoState.ALL.toString(),
                0, 10);
        TypedQuery<Booking> query = em.createQuery("From booking", Booking.class);
        List<Booking> itemRequests = query.getResultList();
        assertThat(allBooking.size(), equalTo(itemRequests.size()));
        try {
            bookingService.getAllBookingByOwnerItemsAndState(itemOwner.getId(), DtoState.ALL.toString(), -1, 10);
        } catch (Exception e) {
            assertThat(e.getMessage(), equalTo("Page index must not be less than zero"));
        }
    }
}
