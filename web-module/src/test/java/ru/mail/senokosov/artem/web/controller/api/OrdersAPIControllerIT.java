package ru.mail.senokosov.artem.web.controller.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import ru.mail.senokosov.artem.service.model.OrderDTO;
import ru.mail.senokosov.artem.web.config.BaseIT;

import java.util.List;

import static ru.mail.senokosov.artem.web.constant.PathConstant.ORDERS_PATH;
import static ru.mail.senokosov.artem.web.constant.PathConstant.REST_API_USER_PATH;

@Sql({"/scripts/clean_orders.sql", "/scripts/init_orders.sql"})
public class OrdersAPIControllerIT extends BaseIT {

    @Test
    void shouldGetAllOrders() {
        HttpEntity<String> request = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<List<OrderDTO>> response = testRestTemplate
                .withBasicAuth("rest@gmail.com", "test")
                .exchange(
                        REST_API_USER_PATH + ORDERS_PATH,
                        HttpMethod.GET,
                        request,
                        new ParameterizedTypeReference<>() {
                        }
                );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody() != null && !response.getBody().isEmpty());
    }

    @Test
    void shouldGetOrderById() {
        HttpEntity<String> request = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<OrderDTO> response = testRestTemplate
                .withBasicAuth("rest@gmail.com", "test")
                .exchange(
                        REST_API_USER_PATH + ORDERS_PATH + "/1",
                        HttpMethod.GET,
                        request,
                        OrderDTO.class
                );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }
}