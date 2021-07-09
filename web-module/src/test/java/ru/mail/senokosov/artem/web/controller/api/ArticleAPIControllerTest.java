package ru.mail.senokosov.artem.web.controller.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.mail.senokosov.artem.service.ArticleService;
import ru.mail.senokosov.artem.service.model.show.ShowArticleDTO;
import ru.mail.senokosov.artem.service.model.show.ShowCommentDTO;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.mail.senokosov.artem.web.constant.PathConstant.ARTICLES_PATH;
import static ru.mail.senokosov.artem.web.constant.PathConstant.REST_API_USER_PATH;


@WebMvcTest(controllers = ArticleAPIController.class,
        excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class)
@ActiveProfiles("test")
class ArticleAPIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService articleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetArticlesAndReturnOk() throws Exception {
        mockMvc.perform(get(REST_API_USER_PATH + ARTICLES_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldVerifyThatTheBusinessLogicWasCalledWhenWeRequestGEtArticles() throws Exception {
        mockMvc.perform(get(REST_API_USER_PATH + ARTICLES_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(articleService, times(1)).getArticles();
    }

    @Test
    void shouldReturnCollectionOfArticlesWhenWeRequestGEtArticles() throws Exception {
        ShowArticleDTO showArticleDTO = new ShowArticleDTO();
        showArticleDTO.setId(1L);
        showArticleDTO.setDate("01-07-2021");
        showArticleDTO.setTitle("Title");
        showArticleDTO.setFirstName("first name");
        showArticleDTO.setLastName("last name");
        showArticleDTO.setShortContent("short content");
        showArticleDTO.setFullContent("full content");
        ShowCommentDTO showCommentDTO = new ShowCommentDTO();
        showCommentDTO.setId(1L);
        showCommentDTO.setFullName("full name");
        showCommentDTO.setDate("01-07-2021");
        showCommentDTO.setFullContent("content");
        showArticleDTO.getComments().add(showCommentDTO);

        List<ShowArticleDTO> articles = Collections.singletonList(showArticleDTO);

        when(articleService.getArticles()).thenReturn(articles);

        MvcResult mvcResult = mockMvc.perform(get(REST_API_USER_PATH + ARTICLES_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).isEqualToIgnoringCase(objectMapper.writeValueAsString(articles));
    }

    @Test
    void shouldReturnArticleWhenWeRequestGEtArticleById() throws Exception {
        ShowArticleDTO showArticleDTO = new ShowArticleDTO();
        Long id = 1L;
        showArticleDTO.setId(1L);
        showArticleDTO.setDate("01-07-2021");
        showArticleDTO.setTitle("Title");
        showArticleDTO.setFirstName("first name");
        showArticleDTO.setLastName("last name");
        showArticleDTO.setShortContent("short content");
        showArticleDTO.setFullContent("full content");
        ShowCommentDTO showCommentDTO = new ShowCommentDTO();
        showCommentDTO.setId(1L);
        showCommentDTO.setFullName("full name");
        showCommentDTO.setDate("01-07-2021");
        showCommentDTO.setFullContent("content");
        showArticleDTO.getComments().add(showCommentDTO);

        when(articleService.getArticleById(id)).thenReturn(showArticleDTO);

        MvcResult mvcResult = mockMvc.perform(get(String.format("%s%s/%s", REST_API_USER_PATH, ARTICLES_PATH, id))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).isEqualToIgnoringCase(objectMapper.writeValueAsString(showArticleDTO));
    }

    @Test
    void should404WhenWeRequestGEtArticleByWrongId() throws Exception {
        ShowArticleDTO showArticleDTO = new ShowArticleDTO();
        Long id = 1L;
        showArticleDTO.setId(1L);
        showArticleDTO.setDate("01-07-2021");
        showArticleDTO.setTitle("Title");
        showArticleDTO.setFirstName("first name");
        showArticleDTO.setLastName("last name");
        showArticleDTO.setShortContent("short content");
        showArticleDTO.setFullContent("full content");
        ShowCommentDTO showCommentDTO = new ShowCommentDTO();
        showCommentDTO.setId(1L);
        showCommentDTO.setFullName("full name");
        showCommentDTO.setDate("01-07-2021");
        showCommentDTO.setFullContent("content");
        showArticleDTO.getComments().add(showCommentDTO);

        when(articleService.getArticleById(id)).thenReturn(showArticleDTO);
        Long wrongId = 2L;

        mockMvc.perform(get(String.format("%s%s/%s", REST_API_USER_PATH, ARTICLES_PATH, wrongId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void should201WhenWeRequestAddArticle() throws Exception {
        mockMvc.perform(post(REST_API_USER_PATH + ARTICLES_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"title\": \"test title\",\n" +
                        "  \"content\": \"test content\",\n" +
                        "  \"sellerId\": 4\n" +
                        "}"))
                .andExpect(status().isCreated());
    }

    @Test
    void should400WhenWeRequestAddArticleWithNotValidField() throws Exception {
        mockMvc.perform(post(REST_API_USER_PATH + ARTICLES_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"title\": \"test title where number of character more 20\",\n" +
                        "  \"content\": \"test content\",\n" +
                        "  \"sellerId\": 4\n" +
                        "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should200WhenWeRequestDeleteArticleById() throws Exception {
        Long id = 1L;
        when(articleService.isDeleteById(id)).thenReturn(true);
        mockMvc.perform(delete(String.format("%s%s/%s", REST_API_USER_PATH, ARTICLES_PATH, id))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void should404WhenWeRequestDeleteArticleByWrongId() throws Exception {
        Long id = 1L;
        when(articleService.isDeleteById(id)).thenReturn(false);
        mockMvc.perform(delete(String.format("%s%s/%s", REST_API_USER_PATH, ARTICLES_PATH, id))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}