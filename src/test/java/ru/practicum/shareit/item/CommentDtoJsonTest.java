package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.AnswerCommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.RequestCommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@JsonTest
public class CommentDtoJsonTest {
    @Autowired
    private JacksonTester<RequestCommentDto> json;
    private RequestCommentDto requestCommentDto;

    @BeforeEach()
    void beforeEach() {
        requestCommentDto = new RequestCommentDto();
        requestCommentDto.setText("text");
        requestCommentDto.setId(1);
    }

    @Test
    void requestCommentDtoTest() throws Exception {
        JsonContent<RequestCommentDto> result = json.write(requestCommentDto);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("text");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
    }

    @Test
    void mapperRequestCommentDtoTest() {
        CommentMapper mapper = Mappers.getMapper(CommentMapper.class);
        assertNull(mapper.toComment(null, null, null, null));
        assertNotNull(mapper.toComment(null, LocalDateTime.now(), null, null));
        assertNotNull(mapper.toComment(null, null, new User(), null));
        assertNotNull(mapper.toComment(null, null, null, new Item()));
        assertNull(mapper.toCommentDto(null));
        assertNotNull(mapper.toCommentDto(new Comment()));
        Comment comment = mapper.toComment(requestCommentDto, LocalDateTime.now(), new User(), new Item());
        assertEquals(requestCommentDto.getId(), comment.getId());
        assertEquals(requestCommentDto.getText(), comment.getText());
        User user = new User();
        user.setId(2);
        user.setEmail("user@email");
        Item item = new Item();
        item.setId(2);
        item.setOwner(user);
        item.setName("item 1");
        item.setDescription("item 1 description");
        item.setAvailable(true);
        comment = mapper.toComment(requestCommentDto, LocalDateTime.now(), user, item);
        assertEquals(requestCommentDto.getId(), comment.getId());
        assertEquals(requestCommentDto.getText(), comment.getText());
        AnswerCommentDto answerCommentDto = mapper.toCommentDto(comment);
        assertEquals(answerCommentDto.getId(), comment.getId());
        assertEquals(answerCommentDto.getText(), comment.getText());
        assertEquals(answerCommentDto.getCreated(), comment.getCreated());
        assertEquals(answerCommentDto.getAuthorName(), comment.getAuthor().getName());
        user.setName("user");
        comment = mapper.toComment(requestCommentDto, LocalDateTime.now(), user, item);
        assertEquals(requestCommentDto.getId(), comment.getId());
        assertEquals(requestCommentDto.getText(), comment.getText());
    }
}
