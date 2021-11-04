package ru.mail.senokosov.artem.web.controller.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import ru.mail.senokosov.artem.service.model.NewsDTO;
import ru.mail.senokosov.artem.web.config.BaseIT;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.mail.senokosov.artem.web.constant.PathConstant.NEWS_PATH;
import static ru.mail.senokosov.artem.web.constant.PathConstant.REST_API_USER_PATH;

@Sql({"/scripts/clean_news.sql", "/scripts/init_news.sql"})
public class NewsAPIControllerIT extends BaseIT {

    @Test
    void shouldGetAllNews() {
        HttpEntity<String> request = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<List<NewsDTO>> response = testRestTemplate
                .withBasicAuth("rest@gmail.com", "test")
                .exchange(
                        REST_API_USER_PATH + NEWS_PATH,
                        HttpMethod.GET,
                        request,
                        new ParameterizedTypeReference<>() {
                        }
                );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() != null && !response.getBody().isEmpty());
    }

    @Test
    void shouldGetNewsById() {
        HttpEntity<String> request = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<NewsDTO> response = testRestTemplate
                .withBasicAuth("rest@gmail.com", "test")
                .exchange(
                        REST_API_USER_PATH + NEWS_PATH + "/1",
                        HttpMethod.GET,
                        request,
                        NewsDTO.class
                );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}