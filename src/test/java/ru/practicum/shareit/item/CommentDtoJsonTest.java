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
    private final CommentMapper mapper = Mappers.getMapper(CommentMapper.class);
    @Autowired
    private JacksonTester<RequestCommentDto> json;
    private RequestCommentDto requestCommentDto;
    private User user;
    private Item item;

    @BeforeEach()
    void beforeEach() {
        requestCommentDto = new RequestCommentDto();
        requestCommentDto.setText("text");
        requestCommentDto.setId(1);
        user = new User();
        user.setId(2);
        user.setEmail("user@email");
        item = new Item();
        item.setId(2);
        item.setOwner(user);
        item.setName("item 1");
        item.setDescription("item 1 description");
        item.setAvailable(true);
    }

    @Test
    void requestCommentDtoTest() throws Exception {
        JsonContent<RequestCommentDto> result = json.write(requestCommentDto);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("text");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
    }

    @Test
    void shouldReturnNotNull() {
        assertNotNull(mapper.toComment(null, LocalDateTime.now(), null, null));
        assertNotNull(mapper.toComment(null, null, new User(), null));
        assertNotNull(mapper.toComment(null, null, null, new Item()));
        assertNotNull(mapper.toCommentDto(new Comment()));
    }

    @Test
    void shouldReturnNull() {
        assertNull(mapper.toComment(null, null, null, null));
        assertNull(mapper.toCommentDto(null));
    }

    @Test
    void shouldReturnNewCommentWithNewUserAndItem() {
        Comment comment = mapper.toComment(requestCommentDto, LocalDateTime.now(), new User(), new Item());
        assertIdAndTextCommentDtoEqualsIdAndTextComment(requestCommentDto, comment);
    }

    @Test
    void shouldReturnNewAnsDtoComment() {
        Comment comment = mapper.toComment(requestCommentDto, LocalDateTime.now(), user, item);
        assertIdAndTextCommentDtoEqualsIdAndTextComment(requestCommentDto, comment);
        AnswerCommentDto answerCommentDto = mapper.toCommentDto(comment);
        assertEquals(answerCommentDto.getId(), comment.getId());
        assertEquals(answerCommentDto.getText(), comment.getText());
        assertEquals(answerCommentDto.getCreated(), comment.getCreated());
        assertEquals(answerCommentDto.getAuthorName(), comment.getAuthor().getName());
    }

    @Test
    void shouldReturnNewCommentWithUserAndItem() {
        Comment comment = mapper.toComment(requestCommentDto, LocalDateTime.now(), user, item);
        assertIdAndTextCommentDtoEqualsIdAndTextComment(requestCommentDto, comment);
        user.setName("user");
        comment = mapper.toComment(requestCommentDto, LocalDateTime.now(), user, item);
        assertIdAndTextCommentDtoEqualsIdAndTextComment(requestCommentDto, comment);
    }

    private void assertIdAndTextCommentDtoEqualsIdAndTextComment(RequestCommentDto requestCommentDto, Comment comment) {
        assertEquals(requestCommentDto.getId(), comment.getId());
        assertEquals(requestCommentDto.getText(), comment.getText());
    }
}
