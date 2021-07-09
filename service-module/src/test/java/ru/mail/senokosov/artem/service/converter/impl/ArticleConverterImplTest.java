package ru.mail.senokosov.artem.service.converter.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.mail.senokosov.artem.repository.model.Article;
import ru.mail.senokosov.artem.repository.model.Comment;
import ru.mail.senokosov.artem.service.converter.CommentConverter;
import ru.mail.senokosov.artem.service.model.add.AddArticleDTO;
import ru.mail.senokosov.artem.service.model.show.ShowArticleDTO;
import ru.mail.senokosov.artem.service.model.show.ShowCommentDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.mail.senokosov.artem.service.constant.FormatConstant.DATE_FORMAT_PATTERN;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ArticleConverterImplTest {

    @Mock
    private CommentConverter commentConverter;
    @InjectMocks
    private ArticleConverterImpl articleConverter;

    @Test
    void shouldConvertArticleToShowArticleDTOAndReturnRightId() {
        Article article = new Article();
        Long id = 1L;
        article.setId(id);
        ShowArticleDTO showArticleDTO = articleConverter.convert(article);

        assertEquals(id, showArticleDTO.getId());
    }

    @Test
    void shouldConvertArticleToShowArticleDTOAndReturnRightTitle() {
        Article article = new Article();
        String title = "test title";
        article.setTitle(title);
        ShowArticleDTO showArticleDTO = articleConverter.convert(article);

        assertEquals(title, showArticleDTO.getTitle());
    }

    @Test
    void shouldConvertArticleToShowArticleDTOAndReturnRightDate() {
        Article article = new Article();
        LocalDateTime localDateTime = LocalDateTime.now();
        article.setLocalDateTime(localDateTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);
        String formatLocalDate = formatter.format(localDateTime);
        ShowArticleDTO showArticleDTO = articleConverter.convert(article);

        assertEquals(formatLocalDate, showArticleDTO.getDate());
    }

    @Test
    void shouldConvertArticleToShowArticleDTOAndReturnRightContent() {
        Article article = new Article();
        String content = "test content";
        article.setFullContent(content);
        ShowArticleDTO showArticleDTO = articleConverter.convert(article);

        assertEquals(content, showArticleDTO.getFullContent());
        assertEquals(content, showArticleDTO.getShortContent());
    }

    @Test
    void shouldConvertArticleToShowArticleDTOAndReturnRightComments() {
        Set<Comment> comments = new HashSet<>();

        Comment comment = new Comment();
        Long commentId = 1L;
        comment.setId(commentId);
        LocalDateTime localDateTimeCommit = LocalDateTime.now();
        comment.setLocalDateTime(localDateTimeCommit);
        String contentComment = "test content comment";
        comment.setFullContent(contentComment);
        comments.add(comment);

        Comment comment2 = new Comment();
        Long commentId2 = 2L;
        comment2.setId(commentId2);
        LocalDateTime localDateTimeCommit2 = LocalDateTime.now();
        comment2.setLocalDateTime(localDateTimeCommit2);
        String contentComment2 = "test content comment2";
        comment2.setFullContent(contentComment2);
        comments.add(comment2);

        Article article = new Article();
        article.setComments(comments);
        List<ShowCommentDTO> commentDTOs = comments.stream()
                .map(commentConverter::convert)
                .collect(Collectors.toList());
        ShowArticleDTO showArticleDTO = articleConverter.convert(article);

        assertEquals(commentDTOs, showArticleDTO.getComments());
    }

    @Test
    void shouldConvertAddArticleDTOToArticleAndReturnRightTitle() {
        AddArticleDTO addArticleDTO = new AddArticleDTO();
        String title = "test";
        addArticleDTO.setTitle(title);

        Article article = articleConverter.convert(addArticleDTO);
        assertEquals(title, article.getTitle());
    }

    @Test
    void shouldConvertAddArticleDTOToArticleAndReturnRightContent() {
        AddArticleDTO addArticleDTO = new AddArticleDTO();
        String content = "test";
        addArticleDTO.setContent(content);

        Article article = articleConverter.convert(addArticleDTO);
        assertEquals(content, article.getFullContent());
    }

    @Test
    void shouldFormatLocalDateTime() {
        LocalDateTime localDateTime = LocalDateTime.of(2021, 7, 1, 00, 00, 00);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);
        String formatData = "2021-07-01 00:00:00";
        assertEquals(formatData, localDateTime.format(formatter));
    }
}