package ru.mail.senokosov.artem.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.mail.senokosov.artem.repository.CommentRepository;
import ru.mail.senokosov.artem.repository.NewsRepository;
import ru.mail.senokosov.artem.repository.UserRepository;
import ru.mail.senokosov.artem.repository.model.News;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.converter.NewsConverter;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.NewsDTO;
import ru.mail.senokosov.artem.service.model.PageDTO;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class NewsServiceTest {

    @Mock
    private NewsRepository newsRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private NewsConverter newsConverter;

    @InjectMocks
    private NewsServiceImpl newsService;

    @Test
    void shouldReturnPageDTOWhenRequestedPageIsValid() {
        when(newsRepository.getCount()).thenReturn(10L);
        when(newsRepository.findAll(anyInt(), anyInt())).thenReturn(Collections.singletonList(new News()));
        when(newsConverter.convert(any(News.class))).thenReturn(new NewsDTO());

        PageDTO result = newsService.getNewsByPage(1);

        assertNotNull(result);
        assertEquals(1, result.getNewses().size());
        verify(newsRepository, times(1)).getCount();
        verify(newsRepository, times(1)).findAll(anyInt(), anyInt());
    }

    @Test
    void shouldReturnNewsDTOWhenNewsIdIsFound() throws ServiceException {
        News news = new News();
        when(newsRepository.findById(anyLong())).thenReturn(news);
        when(newsConverter.convert(news)).thenReturn(new NewsDTO());

        NewsDTO result = newsService.getNewsById(1L);

        assertNotNull(result);
        verify(newsRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowServiceExceptionWhenNewsIdIsNotFound() {
        when(newsRepository.findById(anyLong())).thenReturn(null);

        assertThrows(ServiceException.class, () -> newsService.getNewsById(1L));
    }

    @Test
    void shouldReturnListOfNewsDTOWhenNewsAreAvailable() throws ServiceException {
        when(newsRepository.findAll()).thenReturn(Arrays.asList(new News(), new News()));
        when(newsConverter.convert(any(News.class))).thenAnswer(invocation -> new NewsDTO());

        List<NewsDTO> result = newsService.getAllNews();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoNewsAreFound() throws ServiceException {
        when(newsRepository.findAll()).thenReturn(Collections.emptyList());

        List<NewsDTO> result = newsService.getAllNews();

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnTrueWhenNewsDeletionIsSuccessful() {
        when(newsRepository.findById(anyLong())).thenReturn(new News());

        boolean result = newsService.isDeleteById(1L);

        assertTrue(result);
        verify(commentRepository, times(1)).deleteByNewsId(1L);
        verify(newsRepository, times(1)).removeById(1L);
    }

    @Test
    void shouldReturnFalseWhenNewsToDeleteIsNotFound() {
        when(newsRepository.findById(anyLong())).thenReturn(null);

        boolean result = newsService.isDeleteById(1L);

        assertFalse(result);
    }

    @Test
    void shouldReturnNewsDTOWhenNewsIsSuccessfullyAdded() throws ServiceException {
        String userEmail = "user@example.com";
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(userEmail);
        when(userRepository.findUserByEmail(userEmail)).thenReturn(new User());

        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setTitle("Test News");
        News news = new News();
        when(newsConverter.convert(newsDTO)).thenReturn(news);

        NewsDTO result = newsService.addNews(newsDTO);

        assertNotNull(result);
        verify(newsRepository, times(1)).persist(any(News.class));

        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldHandleInvalidPageNumberGracefully() {
        when(newsRepository.getCount()).thenReturn(10L);

        PageDTO result = newsService.getNewsByPage(100);

        assertNotNull(result);
        assertTrue(result.getNewses().isEmpty(), "Expected no news on an out-of-range page number.");
    }

    @Test
    void shouldThrowServiceExceptionWhenUserForNewsAdditionIsNotFound() {
        String userEmail = "nonexistent@example.com";
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(userEmail);
        when(userRepository.findUserByEmail(userEmail)).thenReturn(null);

        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setTitle("Test News");

        assertThrows(ServiceException.class, () -> newsService.addNews(newsDTO), "Expected ServiceException when user not found.");

        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldThrowServiceExceptionWhenNewsToUpdateIsNotFound() {
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setId(999L);
        newsDTO.setTitle("Updated Title");

        assertThrows(ServiceException.class, () -> newsService.updateNews(newsDTO), "Expected ServiceException when trying to update non-existent news.");
    }

    @Test
    void shouldThrowServiceExceptionWhenErrorOccursInFetchingAllNews() {
        when(newsRepository.findAll()).thenThrow(new RuntimeException("Unexpected error"));

        assertThrows(ServiceException.class, () -> newsService.getAllNews(),
                "Expected getAllNews to throw ServiceException, but it didn't");

        verify(newsRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnFalseWhenExceptionOccursDuringNewsDeletion() {
        Long newsId = 1L;

        when(newsRepository.findById(anyLong())).thenThrow(new RuntimeException("Database error"));

        boolean result = newsService.isDeleteById(newsId);

        assertFalse(result, "Expected isDeleteById to return false when an exception occurs");

        verify(newsRepository, times(1)).findById(newsId);
        verify(commentRepository, never()).deleteByNewsId(anyLong());
        verify(newsRepository, never()).removeById(anyLong());
    }

    @Test
    void shouldThrowServiceExceptionWhenAddingNewsWithoutAuthentication() {
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setTitle("Test News");

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(null);

        assertThrows(ServiceException.class, () -> newsService.addNews(newsDTO),
                "Expected ServiceException to be thrown when adding news without authentication");

        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldThrowServiceExceptionWhenAuthenticatedUserIsNotFoundDuringNewsAddition() {
        String userEmail = "user@example.com";
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setTitle("Unauthenticated News Addition Attempt");

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(userEmail);
        when(userRepository.findUserByEmail(userEmail)).thenReturn(null);

        ServiceException thrown = assertThrows(ServiceException.class, () -> newsService.addNews(newsDTO),
                "Expected ServiceException to be thrown when the user is not found");

        assertTrue(thrown.getMessage().contains(userEmail), "Exception message should contain the email of the missing user");

        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldUpdateNewsSuccessfullyWithValidInput() throws ServiceException {
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setId(1L);
        newsDTO.setTitle("Updated News Title");
        newsDTO.setDateOfCreation("2024-03-20 12:00:00");

        News mockedNews = new News();

        when(newsRepository.findById(newsDTO.getId())).thenReturn(mockedNews);
        doNothing().when(newsRepository).merge(any(News.class));

        newsService.updateNews(newsDTO);

        verify(newsRepository, times(1)).findById(newsDTO.getId());
        verify(newsRepository, times(1)).merge(mockedNews);
        assertEquals("Updated News Title", mockedNews.getTitle(), "The news title should be updated.");
    }

    @Test
    void shouldFailToUpdateNewsWhenDateParsingFails() {
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setId(1L);
        newsDTO.setTitle("News With Bad Date");
        newsDTO.setDateOfCreation("invalid-date");

        News mockedNews = new News();
        when(newsRepository.findById(newsDTO.getId())).thenReturn(mockedNews);

        ServiceException thrown = assertThrows(ServiceException.class,
                () -> newsService.updateNews(newsDTO),
                "Expected ServiceException due to date parsing failure.");

        assertTrue(thrown.getMessage().contains("Error parsing date"), "The exception message should indicate a date parsing error.");
    }

    @Test
    void shouldHandleUnexpectedExceptionsDuringNewsUpdateGracefully() {
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setId(1L);
        newsDTO.setTitle("News Update");
        newsDTO.setDateOfCreation("2024-03-20T12:00");

        when(newsRepository.findById(newsDTO.getId())).thenThrow(new RuntimeException("Unexpected database error"));

        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> newsService.updateNews(newsDTO),
                "Expected RuntimeException due to an unexpected error.");

        assertTrue(thrown.getMessage().contains("Unexpected database error"), "The exception message should indicate an unexpected database error.");
    }

    @Test
    void shouldWrapUnexpectedExceptionsIntoServiceExceptionDuringNewsUpdate() {
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setId(1L);
        newsDTO.setTitle("Some title");
        newsDTO.setDateOfCreation("2023-01-01 00:00:00");

        when(newsRepository.findById(anyLong())).thenReturn(new News());
        doThrow(new RuntimeException("Unexpected database issue")).when(newsRepository).merge(any(News.class));

        ServiceException thrown = assertThrows(ServiceException.class, () -> newsService.updateNews(newsDTO),
                "Expected ServiceException to be thrown due to an unexpected error.");

        assertTrue(thrown.getMessage().contains("Unexpected error occurred while updating news"),
                "Exception message should indicate an unexpected error during update.");
    }
}