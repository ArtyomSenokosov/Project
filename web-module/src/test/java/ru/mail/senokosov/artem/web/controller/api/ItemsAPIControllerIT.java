package ru.mail.senokosov.artem.web.controller.api;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import ru.mail.senokosov.artem.service.model.ItemDTO;
import ru.mail.senokosov.artem.web.config.BaseIT;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.mail.senokosov.artem.web.constant.PathConstant.ITEMS_PATH;
import static ru.mail.senokosov.artem.web.constant.PathConstant.REST_API_USER_PATH;

@Sql({"/scripts/clean_item.sql", "/scripts/init_item.sql"})
class ItemsAPIControllerIT extends BaseIT {

    @Test
    void shouldGetAllItems() {
        HttpEntity<String> request = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<List<ItemDTO>> response = testRestTemplate
                .withBasicAuth("rest@gmail.com", "test")
                .exchange(
                        REST_API_USER_PATH + ITEMS_PATH,
                        HttpMethod.GET,
                        request,
                        new ParameterizedTypeReference<>() {
                        }
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        assertEquals("test title", response.getBody().get(0).getTitle());
        assertEquals("test content", response.getBody().get(0).getContent());
        assertEquals(UUID.fromString("e5b0f808-ccf1-488f-ace7-2d48e50dea5c"), response.getBody().get(0).getUuid());
        assertEquals(0, BigDecimal.valueOf(500.00).compareTo(response.getBody().get(0).getPrice()));
    }

    @Test
    void shouldGetItemById() {
        HttpEntity<String> request = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<ItemDTO> response = testRestTemplate
                .withBasicAuth("rest@gmail.com", "test")
                .exchange(
                        REST_API_USER_PATH + ITEMS_PATH + "/1",
                        HttpMethod.GET,
                        request,
                        new ParameterizedTypeReference<>() {
                        }
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}