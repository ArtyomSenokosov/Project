package ru.mail.senokosov.artem.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mail.senokosov.artem.service.OrderService;
import ru.mail.senokosov.artem.service.model.show.ShowOrderDTO;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Controller
public class OrderController {

    private final OrderService orderService;

    @GetMapping(value = "/seller/orders/page/{pageNo}")
    public String getSellerOrdersByPagination(@PathVariable(value = "pageNo") int pageNo,
                                              @RequestParam("sortField") String sortField,
                                              @RequestParam("sortDir") String sortDir,
                                              Model model) {
        Integer pageSize = 10;

        Page<ShowOrderDTO> page = orderService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<ShowOrderDTO> orders = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listOrders", orders);
        return "seller-orders";
    }

    @GetMapping(value = "/customer/orders/page/{pageNo}")
    public String getCustomerOrdersByPagination(@PathVariable(value = "pageNo") int pageNo,
                                                @RequestParam("sortField") String sortField,
                                                @RequestParam("sortDir") String sortDir,
                                                Model model) {
        Integer pageSize = 10;

        Page<ShowOrderDTO> page = orderService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<ShowOrderDTO> orders = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listOrders", orders);
        return "customer-orders";
    }

    @GetMapping(value = "/seller/order/{id}")
    public String getOrderById(Model model, @PathVariable("id") Long id) {
        ShowOrderDTO order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "order";
    }
}
