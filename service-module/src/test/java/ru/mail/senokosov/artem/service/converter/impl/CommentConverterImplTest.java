package ru.mail.senokosov.artem.service.converter.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.mail.senokosov.artem.repository.model.Article;
import ru.mail.senokosov.artem.repository.model.Comment;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.model.add.AddCommentDTO;
import ru.mail.senokosov.artem.service.model.show.ShowCommentDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CommentConverterImplTest {

    @InjectMocks
    private CommentConverterImpl commentConverter;

    @Test
    void shouldConvertCommentToShowCommentDTOAndReturnNotNullObject() {
        Comment comment = new Comment();
        ShowCommentDTO showCommentDTO = commentConverter.convert(comment);

        assertNotNull(showCommentDTO);
    }

    @Test
    void shouldConvertCommentToShowCommentDTOAndReturnRightDate() {
        Comment comment = new Comment();
        LocalDateTime localDateTime = LocalDateTime.now();
        comment.setLocalDateTime(localDateTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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
    void shouldConvertCommentToShowCommentDTOReturnRightFirstName() {
        Comment comment = new Comment();
        String firstName = "test first name";
        User user = new User();
        user.setFirstname(firstName);
        comment.setUser(user);
        ShowCommentDTO showCommentDTO = commentConverter.convert(comment);

        assertEquals(firstName, showCommentDTO.getFirstName());
    }

    @Test
    void shouldConvertAddCommentDTOAndUserAndArticleToCommentAndReturnNotNullObject() {
        AddCommentDTO addCommentDTO = new AddCommentDTO();
        User user = new User();
        Article article = new Article();
        Comment comment = commentConverter.convert(addCommentDTO, user, article);

        assertNotNull(comment);
    }

    @Test
    void shouldConvertAddCommentDTOAndUserAndArticleToCommentAnsReturnRightUser() {
        User user = new User();
        Comment comment = new Comment();
        comment.setUser(user);
        AddCommentDTO addCommentDTO = new AddCommentDTO();
        Article article = new Article();
        Comment convertComment = commentConverter.convert(addCommentDTO, user, article);

        assertEquals(user, convertComment.getUser());
    }

    @Test
    void shouldConvertAddCommentDTOAndUserAndArticleToCommentAnsReturnRightArticle() {
        Article article = new Article();
        Comment comment = new Comment();
        comment.setArticle(article);
        AddCommentDTO addCommentDTO = new AddCommentDTO();
        User user = new User();
        Comment convertComment = commentConverter.convert(addCommentDTO, user, article);

        assertEquals(user, convertComment.getArticle());
    }
}
