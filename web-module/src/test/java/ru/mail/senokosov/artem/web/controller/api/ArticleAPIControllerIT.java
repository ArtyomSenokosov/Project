package ru.mail.senokosov.artem.web.controller.api;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import ru.mail.senokosov.artem.service.model.add.AddArticleDTO;
import ru.mail.senokosov.artem.service.model.show.ShowArticleDTO;
import ru.mail.senokosov.artem.web.config.BaseIT;

import java.util.List;
import java.util.Objects;

import static junit.framework.TestCase.assertEquals;
import static ru.mail.senokosov.artem.web.constant.PathConstant.ARTICLES_PATH;
import static ru.mail.senokosov.artem.web.constant.PathConstant.REST_API_USER_PATH;

@Sql({"/scripts/clean_article.sql", "/scripts/init_article.sql"})
class ArticleAPIControllerIT extends BaseIT {

    @Test
    void shouldGetAllArticles() {
        HttpEntity<String> request = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<List<ShowArticleDTO>> response = testRestTemplate
                .withBasicAuth("rest@gmail.com", "test")
                .exchange(
                        REST_API_USER_PATH + ARTICLES_PATH,
                        HttpMethod.GET,
                        request,
                        new ParameterizedTypeReference<>() {
                        }
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        assertEquals("2021-07-01 00:00:00", response.getBody().get(0).getDate());
        assertEquals("test title", response.getBody().get(0).getTitle());
        assertEquals("test content", response.getBody().get(0).getShortContent());
    }

    @Test
    void shouldGetArticleById() {
        HttpEntity<String> request = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<ShowArticleDTO> response = testRestTemplate
                .withBasicAuth("rest@gmail.com", "test")
                .exchange(
                        REST_API_USER_PATH + ARTICLES_PATH + "/1",
                        HttpMethod.GET,
                        request,
                        new ParameterizedTypeReference<>() {
                        }
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("2021-07-01 00:00:00", Objects.requireNonNull(response.getBody()).getDate());
        assertEquals("test title", response.getBody().getTitle());
        assertEquals("test content", response.getBody().getShortContent());
    }

    @Test
    void shouldDeleteArticleById() {
        HttpEntity<String> request = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<ShowArticleDTO> response = testRestTemplate
                .withBasicAuth("rest@gmail.com", "test")
                .exchange(
                        REST_API_USER_PATH + ARTICLES_PATH + "/1",
                        HttpMethod.DELETE,
                        request,
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldAddArticleAndReturnRightShowArticle() {
        AddArticleDTO addArticleDTO = new AddArticleDTO();
        addArticleDTO.setTitle("test title");
        addArticleDTO.setContent("test content");
        addArticleDTO.setSellerId(1L);
        HttpEntity<AddArticleDTO> httpEntity = new HttpEntity<>(addArticleDTO);
        ResponseEntity<AddArticleDTO> response = testRestTemplate
                .withBasicAuth("rest@gmail.com", "test")
                .exchange(
                        REST_API_USER_PATH + ARTICLES_PATH,
                        HttpMethod.POST,
                        httpEntity,
                        AddArticleDTO.class
                );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}