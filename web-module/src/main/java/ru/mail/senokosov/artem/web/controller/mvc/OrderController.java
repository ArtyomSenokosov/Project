package ru.mail.senokosov.artem.web.controller.mvc;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mail.senokosov.artem.service.OrderService;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.PageDTO;
import ru.mail.senokosov.artem.service.model.show.ShowOrderDTO;

import static ru.mail.senokosov.artem.web.constant.PathConstant.*;

@Log4j2
@RequiredArgsConstructor
@Controller
public class OrderController {

    private final OrderService orderService;

    @GetMapping(value = {CUSTOMER_PATH + ORDERS_PATH,
            SELLER_PATH + ORDERS_PATH})
    public String getOrdersByPagination(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        PageDTO pageDTO = orderService.getOrdersByPage(page);
        model.addAttribute("pageDTO", pageDTO);
        return "orders";
    }

    @GetMapping(value = SELLER_PATH + ORDERS_PATH + "/{id}")
    public String getOrderById(Model model, @PathVariable("id") Long id) throws ServiceException {
        ShowOrderDTO order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "order";
    }

    @PostMapping(value = SELLER_PATH + ORDERS_PATH + "/{id}/change-status")
    public String changeStatus(@RequestParam("status") String status,
                               @PathVariable Long id) throws ServiceException {
        orderService.changeStatusById(status, id);
        return "redirect:/seller/orders";
    }
}
