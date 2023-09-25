package ru.practicum.shareit.itemRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoAns;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
public class ItemRequestControllerTest {
    @MockBean
    ItemRequestService itemRequestService;
    @Autowired
    MockMvc mvc;
    ItemRequestDtoAns itemRequestDtoAns;
    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void beforeEach() {
        itemRequestDtoAns = new ItemRequestDtoAns();
        itemRequestDtoAns.setItems(new ArrayList<>());
        itemRequestDtoAns.setId(1);
        itemRequestDtoAns.setCreated(LocalDateTime.of(2024, 12, 13, 3, 42, 05));
        itemRequestDtoAns.setDescription("description");
    }

    @Test
    void shouldAddBooking() throws Exception {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("description");
        when(itemRequestService.addRequest(any(), anyInt()))
                .thenReturn(itemRequestDtoAns);
        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDtoAns.getId()), Integer.class))
                .andExpect(jsonPath("$.created", is(itemRequestDtoAns.getCreated().toString())))
                .andExpect(jsonPath("$.items", is(itemRequestDtoAns.getItems())))
                .andExpect(jsonPath("$.description", is(itemRequestDtoAns.getDescription())));
    }

    @Test
    void shouldFindRequestById() throws Exception {
        when(itemRequestService.findRequestById(anyInt(), anyInt()))
                .thenReturn(itemRequestDtoAns);
        mvc.perform(get("/requests/{requestId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDtoAns.getId()), Integer.class))
                .andExpect(jsonPath("$.created", is(itemRequestDtoAns.getCreated().toString())))
                .andExpect(jsonPath("$.items", is(itemRequestDtoAns.getItems())))
                .andExpect(jsonPath("$.description", is(itemRequestDtoAns.getDescription())));
    }

    @Test
    void shouldFindRequestsByUserId() throws Exception {
        when(itemRequestService.findRequestsByUserId(anyInt()))
                .thenReturn(List.of(itemRequestDtoAns));
        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void shouldFindAllRequests() throws Exception {
        when(itemRequestService.findAllRequests(anyInt(), anyInt(), anyInt()))
                .thenReturn(List.of(itemRequestDtoAns));
        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }
}
