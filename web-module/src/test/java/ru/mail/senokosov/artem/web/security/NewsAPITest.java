package ru.mail.senokosov.artem.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.mail.senokosov.artem.service.NewsService;
import ru.mail.senokosov.artem.service.model.NewsDTO;
import ru.mail.senokosov.artem.web.config.TestSecurityConfig;
import ru.mail.senokosov.artem.web.constant.PathConstant;
import ru.mail.senokosov.artem.web.controller.api.NewsAPIController;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NewsAPIController.class)
@Import(TestSecurityConfig.class)
public class NewsAPITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsService newsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(authorities = "SECURE_API_USER")
    void shouldReturnOkWhenNewsFetchedWithAuthorization() throws Exception {
        NewsDTO newsDTO = new NewsDTO();
        when(newsService.getAllNews()).thenReturn(Collections.singletonList(newsDTO));

        mockMvc.perform(MockMvcRequestBuilders.get(PathConstant.REST_API_USER_PATH + PathConstant.NEWS_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void shouldReturnUnauthorizedWhenNewsFetchedWithoutAuthorization() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PathConstant.REST_API_USER_PATH + PathConstant.NEWS_PATH))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "SECURE_API_USER")
    void shouldReturnOkWhenNewsFetchedByIdWithAuthorization() throws Exception {
        Long newsId = 1L;
        NewsDTO newsDTO = new NewsDTO();
        when(newsService.getNewsById(newsId)).thenReturn(newsDTO);

        mockMvc.perform(MockMvcRequestBuilders.get(PathConstant.REST_API_USER_PATH + PathConstant.NEWS_PATH + "/" + newsId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    @WithAnonymousUser
    void shouldReturnUnauthorizedWhenNewsFetchedByIdWithoutAuthorization() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PathConstant.REST_API_USER_PATH + PathConstant.NEWS_PATH + "/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "SECURE_API_USER")
    void shouldReturnCreatedWhenNewsAddedWithValidData() throws Exception {
        NewsDTO newNews = new NewsDTO();
        newNews.setTitle("Test News");
        newNews.setFullContent("This is a test news.");
        when(newsService.addNews(any(NewsDTO.class))).thenReturn(newNews);

        mockMvc.perform(post(PathConstant.REST_API_USER_PATH + PathConstant.NEWS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newNews))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(newNews.getTitle()));
    }

    @Test
    @WithMockUser(authorities = "SECURE_API_USER")
    void shouldReturnBadRequestWhenNewsAddedWithInvalidData() throws Exception {
        mockMvc.perform(post(PathConstant.REST_API_USER_PATH + PathConstant.NEWS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    @WithMockUser(authorities = "SECURE_API_USER")
    void shouldReturnOkWhenNewsDeletedWithExistingId() throws Exception {
        Long newsId = 1L;
        when(newsService.isDeleteById(newsId)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete(PathConstant.REST_API_USER_PATH + PathConstant.NEWS_PATH + "/" + newsId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("News article with id " + newsId + " was successfully deleted."));
    }

    @Test
    @WithMockUser(authorities = "SECURE_API_USER")
    void shouldReturnNotFoundWhenNewsDeletedWithNonExistingId() throws Exception {
        Long newsId = 2L;
        when(newsService.isDeleteById(newsId)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete(PathConstant.REST_API_USER_PATH + PathConstant.NEWS_PATH + "/" + newsId))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("News article with id " + newsId + " was not found."));
    }
}