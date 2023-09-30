package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDtoAnswer;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
public class BookingControllerTest {
    @MockBean
    private BookingService bookingService;
    @Autowired
    private MockMvc mvc;
    private BookingDtoAnswer bookingDtoAnswer;
    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void beforeEach() {
        bookingDtoAnswer = new BookingDtoAnswer();
        bookingDtoAnswer.setId(1);
        bookingDtoAnswer.setStatus(BookingStatus.WAITING);
        bookingDtoAnswer.setEnd(LocalDateTime.MAX);
        bookingDtoAnswer.setStart(LocalDateTime.of(2024, 12, 13, 3, 42, 05));
    }

    @Test
    void shouldAddBooking() throws Exception {
        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest();
        bookingDtoRequest.setItemId(1);
        bookingDtoRequest.setEnd(bookingDtoAnswer.getEnd());
        bookingDtoRequest.setId(1);
        bookingDtoRequest.setStart(bookingDtoAnswer.getStart());
        when(bookingService.addBooking(any(), anyInt()))
                .thenReturn(bookingDtoAnswer);
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoAnswer.getId()), Integer.class))
                .andExpect(jsonPath("$.start", is(bookingDtoAnswer.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDtoAnswer.getEnd().toString())))
                .andExpect(jsonPath("$.status", is(bookingDtoAnswer.getStatus().toString())));
    }

    @Test
    void shouldGetBookingById() throws Exception {
        when(bookingService.getBookingById(anyInt(), anyInt()))
                .thenReturn(bookingDtoAnswer);
        mvc.perform(get("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoAnswer.getId()), Integer.class))
                .andExpect(jsonPath("$.start", is(bookingDtoAnswer.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDtoAnswer.getEnd().toString())))
                .andExpect(jsonPath("$.status", is(bookingDtoAnswer.getStatus().toString())));
    }

    @Test
    void shouldApprovedBooking() throws Exception {
        when(bookingService.approvedBooking(anyInt(), anyInt(), anyBoolean()))
                .thenReturn(bookingDtoAnswer);
        mvc.perform(patch("/bookings/{bookingId}", 1)
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoAnswer.getId()), Integer.class))
                .andExpect(jsonPath("$.start", is(bookingDtoAnswer.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDtoAnswer.getEnd().toString())))
                .andExpect(jsonPath("$.status", is(bookingDtoAnswer.getStatus().toString())));
    }

    @Test
    void shouldGetAllBookingByState() throws Exception {
        when(bookingService.getAllBookingByState(anyInt(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDtoAnswer));
        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void shouldGetAllBookingByOwnerItemsAndState() throws Exception {
        when(bookingService.getAllBookingByOwnerItemsAndState(anyInt(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDtoAnswer));
        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }
}
