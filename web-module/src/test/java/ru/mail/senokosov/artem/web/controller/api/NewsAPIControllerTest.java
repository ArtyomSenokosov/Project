package ru.mail.senokosov.artem.web.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.mail.senokosov.artem.service.NewsService;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.NewsDTO;
import ru.mail.senokosov.artem.web.config.TestSecurityConfig;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.mail.senokosov.artem.web.constant.PathConstant.NEWS_PATH;
import static ru.mail.senokosov.artem.web.constant.PathConstant.REST_API_USER_PATH;

@WebMvcTest(controllers = NewsAPIController.class)
@WithMockUser
@Import(TestSecurityConfig.class)
class NewsAPIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsService newsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnNoContentWhenNewsListIsEmpty() throws Exception {
        when(newsService.getAllNews()).thenReturn(Collections.emptyList());

        mockMvc.perform(get(REST_API_USER_PATH + NEWS_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnInternalServerErrorWhenRetrievingNewsFails() throws Exception {
        when(newsService.getAllNews()).thenThrow(RuntimeException.class);

        mockMvc.perform(get(REST_API_USER_PATH + NEWS_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldReturnNewsByIdAndStatusOk() throws Exception {
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setId(1L);
        newsDTO.setTitle("Breaking News");
        newsDTO.setFullContent("Details about the breaking news");

        when(newsService.getNewsById(1L)).thenReturn(newsDTO);

        mockMvc.perform(get(REST_API_USER_PATH + NEWS_PATH + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Breaking News"));
    }

    @Test
    void shouldReturnNotFoundWhenNewsByIdDoesNotExist() throws Exception {
        when(newsService.getNewsById(anyLong())).thenThrow(new ServiceException("News not found"));

        mockMvc.perform(get(REST_API_USER_PATH + NEWS_PATH + "/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewsAndReturnStatusCreated() throws Exception {
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setTitle("New News");
        newsDTO.setFullContent("Description of the new news");

        NewsDTO savedNewsDTO = new NewsDTO();
        savedNewsDTO.setId(1L);
        savedNewsDTO.setTitle(newsDTO.getTitle());
        savedNewsDTO.setFullContent(newsDTO.getFullContent());

        when(newsService.addNews(any(NewsDTO.class))).thenReturn(savedNewsDTO);

        mockMvc.perform(post(REST_API_USER_PATH + NEWS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newsDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("New News"));
    }

    @Test
    void shouldDeleteNewsAndReturnStatusOk() throws Exception {
        when(newsService.isDeleteById(1L)).thenReturn(true);

        mockMvc.perform(delete(REST_API_USER_PATH + NEWS_PATH + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentNews() throws Exception {
        when(newsService.isDeleteById(anyLong())).thenReturn(false);

        mockMvc.perform(delete(REST_API_USER_PATH + NEWS_PATH + "/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}