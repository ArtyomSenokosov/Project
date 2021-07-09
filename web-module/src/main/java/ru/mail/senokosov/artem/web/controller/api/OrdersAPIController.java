package ru.mail.senokosov.artem.web.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mail.senokosov.artem.service.OrderService;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.show.ShowOrderDTO;

import java.util.List;
import java.util.Objects;

import static ru.mail.senokosov.artem.web.constant.PathConstant.ORDERS_PATH;
import static ru.mail.senokosov.artem.web.constant.PathConstant.REST_API_USER_PATH;

@RestController
@RequiredArgsConstructor
@RequestMapping(REST_API_USER_PATH)
public class OrdersAPIController {

    private final OrderService orderService;

    @GetMapping(ORDERS_PATH)
    public ResponseEntity<List<ShowOrderDTO>> getOrders() {
        List<ShowOrderDTO> orders = orderService.getOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping(value = ORDERS_PATH + "/{id}")
    public ResponseEntity<ShowOrderDTO> getOrderById(@PathVariable Long id) throws ServiceException {
        ShowOrderDTO orderById = orderService.getOrderById(id);
        if (Objects.nonNull(orderById)) {
            return new ResponseEntity<>(orderById, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}