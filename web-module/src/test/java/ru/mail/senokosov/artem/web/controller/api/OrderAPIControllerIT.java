package ru.mail.senokosov.artem.web.controller.api;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import ru.mail.senokosov.artem.service.model.show.ShowOrderDTO;
import ru.mail.senokosov.artem.web.config.BaseIT;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.mail.senokosov.artem.web.constant.PathConstant.ORDERS_PATH;
import static ru.mail.senokosov.artem.web.constant.PathConstant.REST_API_USER_PATH;

@Sql({"/scripts/clean_orders.sql", "/scripts/init_orders.sql"})
class OrderAPIControllerIT extends BaseIT {

    @Test
    void shouldGetAllOrders() {
        HttpEntity<String> request = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<List<ShowOrderDTO>> response = testRestTemplate
                .withBasicAuth("rest@gmail.com", "test")
                .exchange(
                        REST_API_USER_PATH + ORDERS_PATH,
                        HttpMethod.GET,
                        request,
                        new ParameterizedTypeReference<>() {
                        }
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        assertEquals(UUID.fromString("de05425c-da35-45ba-be2f-61284704662e"), response.getBody().get(0).getNumberOfOrder());
        assertEquals(BigDecimal.valueOf(500), response.getBody().get(0).getTotalPrice());
    }

    @Test
    void shouldGetArticleById() {
        HttpEntity<String> request = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<ShowOrderDTO> response = testRestTemplate
                .withBasicAuth("rest@gmail.com", "test")
                .exchange(
                        REST_API_USER_PATH + ORDERS_PATH + "/1",
                        HttpMethod.GET,
                        request,
                        new ParameterizedTypeReference<>() {
                        }
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(BigDecimal.valueOf(500), Objects.requireNonNull(response.getBody()).getTotalPrice());
        assertEquals(UUID.fromString("de05425c-da35-45ba-be2f-61284704662e"), (response.getBody()).getNumberOfOrder());
    }
}