package ru.mail.senokosov.artem.web.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mail.senokosov.artem.service.OrderService;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.OrderDTO;

import java.util.List;

import static ru.mail.senokosov.artem.web.constant.PathConstant.ORDERS_PATH;
import static ru.mail.senokosov.artem.web.constant.PathConstant.REST_API_USER_PATH;

@Slf4j
@RestController
@RequestMapping(REST_API_USER_PATH)
@RequiredArgsConstructor
public class OrdersAPIController {

    private final OrderService orderService;

    @GetMapping(ORDERS_PATH)
    public ResponseEntity<List<OrderDTO>> getOrders() {
        try {
            List<OrderDTO> orders = orderService.getAllOrders();
            if (orders.isEmpty()) {
                log.info("No orders found.");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception exception) {
            log.error("Error retrieving orders: {}", exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = ORDERS_PATH + "/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        try {
            OrderDTO orderById = orderService.getOrderById(id);
            if (orderById != null) {
                return new ResponseEntity<>(orderById, HttpStatus.OK);
            } else {
                log.warn("Order with id: {} was not found.", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ServiceException exception) {
            log.error("Error retrieving order by id: {}", exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}