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
import ru.mail.senokosov.artem.repository.model.News;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.converter.CommentConverter;
import ru.mail.senokosov.artem.service.model.CommentDTO;
import ru.mail.senokosov.artem.service.model.NewsDTO;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static ru.mail.senokosov.artem.service.constant.NewsConstant.MAXIMUM_CHARS_FOR_SHORT_CONTENT_FIELD;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class NewsConverterImplTest {

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private CommentConverter commentConverter;

    @InjectMocks
    private NewsConverterImpl newsConverter;

    private News news;
    private NewsDTO newsDTO;
    private CommentDTO commentDTO;

    @BeforeEach
    void setUp() {
        LocalDateTime creationDate = LocalDateTime.of(2024, 3, 20, 12, 0);
        LocalDateTime lastUpdateDate = LocalDateTime.of(2024, 3, 21, 12, 0);

        User user = new User();
        user.setFirstName("Jane");
        user.setLastName("Doe");

        Comment comment = new Comment();
        comment.setDateOfCreation(creationDate);
        comment.setContent("This is a test comment");
        comment.setUser(user);

        commentDTO = new CommentDTO();
        commentDTO.setDateOfCreation("2024-03-20T12:00");
        commentDTO.setFullContent("This is a test comment");
        commentDTO.setFirstName("Jane");
        commentDTO.setLastName("Doe");

        Set<Comment> comments = new HashSet<>();
        comments.add(comment);

        news = new News();
        news.setTitle("Sample News Title");
        news.setContent("This is sample news content");
        news.setDateOfCreation(creationDate);
        news.setLastDateUpdate(lastUpdateDate);
        news.setUser(user);
        news.setComments(comments);

        newsDTO = new NewsDTO();
        newsDTO.setTitle("Sample News Title");
        newsDTO.setFullContent("This is sample news content");
    }

    @Test
    void shouldCorrectlyConvertNewsToDTOWithAllFieldsSet() {
        when(modelMapper.map(any(News.class), any())).thenReturn(newsDTO);
        when(commentConverter.convert(any(Comment.class))).thenReturn(commentDTO);

        NewsDTO resultDTO = newsConverter.convert(news);

        assertEquals("Sample News Title", resultDTO.getTitle());
        assertEquals("This is sample news content", resultDTO.getFullContent());

        assertEquals("2024-03-21 12:00:00", resultDTO.getDateOfCreation());

        assertEquals("Jane", resultDTO.getFirstName());
        assertEquals("Doe", resultDTO.getLastName());

        assertEquals(1, resultDTO.getComments().size());
        assertEquals("This is a test comment", resultDTO.getComments().get(0).getFullContent());
    }

    @Test
    void shouldCorrectlyConvertDTOToNewsWithAllFieldsSet() {
        when(modelMapper.map(any(NewsDTO.class), any())).thenReturn(news);

        News resultNews = newsConverter.convert(newsDTO);

        assertEquals("Sample News Title", resultNews.getTitle());
        assertEquals("This is sample news content", resultNews.getContent());
    }

    @Test
    void shouldUseLastUpdateDateWhenPresent() {
        LocalDateTime lastUpdateDate = LocalDateTime.of(2024, 3, 22, 12, 0);
        news.setLastDateUpdate(lastUpdateDate);

        when(modelMapper.map(any(News.class), any())).thenReturn(newsDTO);
        when(commentConverter.convert(any(Comment.class))).thenReturn(commentDTO);

        NewsDTO resultDTO = newsConverter.convert(news);

        assertEquals("2024-03-22 12:00:00", resultDTO.getDateOfCreation());
    }

    @Test
    void shouldFallbackToCreationDateWhenLastUpdateDateIsNull() {
        news.setLastDateUpdate(null);

        when(modelMapper.map(any(News.class), any())).thenReturn(newsDTO);
        when(commentConverter.convert(any(Comment.class))).thenReturn(commentDTO);

        NewsDTO resultDTO = newsConverter.convert(news);

        assertEquals("2024-03-20 12:00:00", resultDTO.getDateOfCreation());
    }

    @Test
    void shouldNotAddCommentsWhenEmpty() {
        news.setComments(new HashSet<>());

        when(modelMapper.map(any(News.class), any())).thenReturn(newsDTO);

        NewsDTO resultDTO = newsConverter.convert(news);

        assertTrue(resultDTO.getComments().isEmpty());
    }

    @Test
    void shouldAddShortContentCorrectlyWhenContentExceedsMaxLength() {
        String longContent = String.join("", Collections.nCopies(250, "a"));
        news.setContent(longContent);

        when(modelMapper.map(any(News.class), any())).thenReturn(newsDTO);

        NewsDTO resultDTO = newsConverter.convert(news);

        assertEquals(MAXIMUM_CHARS_FOR_SHORT_CONTENT_FIELD, resultDTO.getShortContent().length());
    }

    @Test
    void shouldSetFullContentToShortContentWhenNotExceedingMaxLength() {
        String shortContent = "Short content";
        news.setContent(shortContent);

        when(modelMapper.map(any(News.class), any())).thenReturn(newsDTO);

        NewsDTO resultDTO = newsConverter.convert(news);

        assertEquals(shortContent, resultDTO.getShortContent());
    }

    @Test
    void shouldPopulateUserFieldsInNewsDTOWhenUserIsPresent() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        news.setUser(user);

        when(modelMapper.map(any(News.class), any())).thenReturn(newsDTO);
        when(commentConverter.convert(any(Comment.class))).thenReturn(commentDTO);

        NewsDTO resultDTO = newsConverter.convert(news);

        assertEquals("John", resultDTO.getFirstName(), "FirstName should be set");
        assertEquals("Doe", resultDTO.getLastName(), "LastName should be set");
    }

    @Test
    void shouldNotPopulateUserFieldsInNewsDTOWhenUserIsAbsent() {
        news.setUser(null);

        when(modelMapper.map(any(News.class), any())).thenReturn(newsDTO);
        when(commentConverter.convert(any(Comment.class))).thenReturn(commentDTO);

        NewsDTO resultDTO = newsConverter.convert(news);

        assertNull(resultDTO.getFirstName(), "FirstName should not be set when user is absent");
        assertNull(resultDTO.getLastName(), "LastName should not be set when user is absent");
    }
}