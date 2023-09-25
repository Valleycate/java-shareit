package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.AnsItemsDto;
import ru.practicum.shareit.item.dto.AnswerCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.RequestCommentDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
public class ItemControllerTest {
    @MockBean
    ItemService itemService;
    @Autowired
    MockMvc mvc;
    ItemDto itemDto;
    AnsItemsDto ansItemsDto;
    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void beforeEach() {
        itemDto = new ItemDto();
        itemDto.setId(1);
        itemDto.setName("itemDto");
        itemDto.setDescription("itemDto description");
        itemDto.setAvailable(true);
        ansItemsDto = new AnsItemsDto();
        ansItemsDto.setId(1);
        ansItemsDto.setName("ansItemsDto");
        ansItemsDto.setDescription("ansItemsDto description");
        ansItemsDto.setAvailable(true);
    }

    @Test
    void shouldAddItem() throws Exception {
        when(itemService.addItem(any(), anyInt()))
                .thenReturn(itemDto);
        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    void shouldUpdateItems() throws Exception {
        when(itemService.updateItem(any(), anyInt(), anyInt()))
                .thenReturn(itemDto);
        mvc.perform(patch("/items/{itemId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    void shouldFindItemById() throws Exception {
        when(itemService.findItem(anyInt(), anyInt()))
                .thenReturn(ansItemsDto);

        mvc.perform(get("/items/{itemId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(ansItemsDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(ansItemsDto.getName())))
                .andExpect(jsonPath("$.description", is(ansItemsDto.getDescription())))
                .andExpect(jsonPath("$.available", is(ansItemsDto.getAvailable())));
    }

    @Test
    void shouldFindAllItemsUser() throws Exception {
        when(itemService.findAllItemsByUser(anyInt()))
                .thenReturn(List.of(ansItemsDto));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void shouldFindItemsByText() throws Exception {
        when(itemService.searchItem(anyString()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search", 1)
                        .param("text", "something")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void shouldAddComment() throws Exception {
        AnswerCommentDto commentDto = new AnswerCommentDto();
        commentDto.setId(1);
        commentDto.setCreated(LocalDateTime.of(2008, 12, 13, 3, 42, 05));
        commentDto.setAuthorName("author");
        commentDto.setText("text");
        RequestCommentDto request = new RequestCommentDto();
        request.setId(commentDto.getId());
        request.setText(commentDto.getText());
        when(itemService.addComment(any(), anyInt(), anyInt()))
                .thenReturn(commentDto);
        mvc.perform(post("/items/{itemId}/comment", 1)
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Integer.class))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.created", is(commentDto.getCreated().toString())));
    }
}
