package ru.mail.senokosov.artem.service.converter.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import ru.mail.senokosov.artem.repository.model.Comment;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.model.CommentDTO;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CommentConverterImplTest {

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CommentConverterImpl commentConverter;

    private Comment comment;
    private CommentDTO commentDTO;

    @BeforeEach
    void setUp() {
        LocalDateTime testDate = LocalDateTime.of(2024, 3, 20, 12, 0);
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");

        comment = new Comment();
        comment.setDateOfCreation(testDate);
        comment.setContent("This is a test comment");
        comment.setUser(user);

        commentDTO = new CommentDTO();
        commentDTO.setDateOfCreation("2024-03-20T12:00");
        commentDTO.setFullContent("This is a test comment");
        commentDTO.setFirstName("John");
        commentDTO.setLastName("Doe");
    }

    @Test
    void shouldCorrectlyConvertToDTOWithAllFieldsSet() {
        when(modelMapper.map(comment, CommentDTO.class)).thenReturn(commentDTO);
        CommentDTO resultDTO = commentConverter.convert(comment);

        assertEquals("2024-03-20 12:00:00", resultDTO.getDateOfCreation());
        assertEquals("This is a test comment", resultDTO.getFullContent());
        assertEquals("John", resultDTO.getFirstName());
        assertEquals("Doe", resultDTO.getLastName());
    }

    @Test
    void shouldCorrectlyConvertToEntityFromDTO() {
        when(modelMapper.map(commentDTO, Comment.class)).thenReturn(comment);
        Comment resultComment = commentConverter.convert(commentDTO);

        assertEquals("This is a test comment", resultComment.getContent());
    }

    @Test
    void shouldCorrectlyConvertToDTOWhenDateIsNotNull() {
        LocalDateTime testDate = LocalDateTime.of(2024, 3, 20, 12, 0);
        Comment comment = new Comment();
        comment.setDateOfCreation(testDate);
        comment.setContent("Sample content");
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        comment.setUser(user);

        CommentDTO mockCommentDTO = new CommentDTO();
        mockCommentDTO.setFullContent("Sample content");
        mockCommentDTO.setFirstName("John");
        mockCommentDTO.setLastName("Doe");

        when(modelMapper.map(comment, CommentDTO.class)).thenReturn(mockCommentDTO);

        CommentDTO resultDTO = commentConverter.convert(comment);

        assertEquals("Sample content", resultDTO.getFullContent());
        assertEquals("John", resultDTO.getFirstName());
        assertEquals("Doe", resultDTO.getLastName());
    }

    @Test
    void shouldSetDateOfCreationToNullInDTOWhenDateIsNull() {
        comment.setDateOfCreation(null);

        when(modelMapper.map(any(Comment.class), any())).thenReturn(new CommentDTO());

        CommentDTO resultDTO = commentConverter.convert(comment);
        assertNull(resultDTO.getDateOfCreation());
    }

    @Test
    void shouldCorrectlyConvertToDTOWhenUserIsNotNull() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        comment.setUser(user);

        when(modelMapper.map(any(Comment.class), any())).thenReturn(new CommentDTO());

        CommentDTO resultDTO = commentConverter.convert(comment);
        assertEquals("John", resultDTO.getFirstName());
        assertEquals("Doe", resultDTO.getLastName());
    }

    @Test
    void shouldNotSetUserRelatedFieldsInDTOWhenUserIsNull() {
        comment.setUser(null);

        when(modelMapper.map(any(Comment.class), any())).thenReturn(new CommentDTO());

        CommentDTO resultDTO = commentConverter.convert(comment);
        assertNull(resultDTO.getFirstName());
        assertNull(resultDTO.getLastName());
    }
}