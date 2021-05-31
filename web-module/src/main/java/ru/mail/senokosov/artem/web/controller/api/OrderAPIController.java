package ru.mail.senokosov.artem.web.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mail.senokosov.artem.service.OrderService;
import ru.mail.senokosov.artem.service.model.show.ShowArticleDTO;
import ru.mail.senokosov.artem.service.model.show.ShowOrderDTO;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
public class OrderAPIController {

    private final OrderService orderService;

    @GetMapping(value = "/orders")
    public List<ShowOrderDTO> getOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping(value = "/orders/{id}")
    public ShowOrderDTO getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }
}
