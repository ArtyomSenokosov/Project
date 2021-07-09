package ru.mail.senokosov.artem.service.converter.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.mail.senokosov.artem.repository.model.Comment;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.model.add.AddCommentDTO;
import ru.mail.senokosov.artem.service.model.show.ShowCommentDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.mail.senokosov.artem.service.constant.FormatConstant.DATE_FORMAT_PATTERN;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CommentConverterImplTest {

    @InjectMocks
    private CommentConverterImpl commentConverter;

    @Test
    void shouldConvertCommentToShowCommentDTOAndReturnRightId() {
        Comment comment = new Comment();
        Long id = 1L;
        comment.setId(id);
        ShowCommentDTO showCommentDTO = commentConverter.convert(comment);

        assertEquals(id, showCommentDTO.getId());
    }

    @Test
    void shouldConvertCommentToShowCommentDTOAndReturnRightDate() {
        Comment comment = new Comment();
        LocalDateTime localDateTime = LocalDateTime.now();
        comment.setLocalDateTime(localDateTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);
        String formatLocalDate = formatter.format(localDateTime);
        ShowCommentDTO showCommentDTO = commentConverter.convert(comment);

        assertEquals(formatLocalDate, showCommentDTO.getDate());
    }

    @Test
    void shouldConvertCommentToShowCommentDTOAndReturnRightContent() {
        Comment comment = new Comment();
        String content = "test content";
        comment.setFullContent(content);
        ShowCommentDTO showCommentDTO = commentConverter.convert(comment);

        assertEquals(content, showCommentDTO.getFullContent());
    }

    @Test
    void shouldConvertCommentToShowCommentDTOAndReturnRightFullName() {
        Comment comment = new Comment();
        String firstName = "test first name";
        String lastName = "test last name";
        String fullName = "test first name test last name";
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        comment.setUser(user);
        ShowCommentDTO showCommentDTO = commentConverter.convert(comment);

        assertEquals(fullName, showCommentDTO.getFullName());
    }

    @Test
    void shouldConvertAddCommentDTOToCommentAndReturnDate() {
        AddCommentDTO addCommentDTO = new AddCommentDTO();
        Comment comment = commentConverter.convert(addCommentDTO);

        assertNotNull(comment.getLocalDateTime());
    }
}