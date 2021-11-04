package ru.mail.senokosov.artem.web.controller.mvc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mail.senokosov.artem.service.OrderService;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.OrderDTO;
import ru.mail.senokosov.artem.service.model.PageDTO;

import static ru.mail.senokosov.artem.web.constant.PathConstant.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping(value = {SELLER_PATH + ORDERS_PATH,
            ADMIN_PATH + ORDERS_PATH,
            CUSTOMER_PATH + ORDERS_PATH})
    public String getOrdersByPagination(Model model, @RequestParam(value = "page", defaultValue = "1") int page,
                                        Authentication authentication) {
        boolean isCustomerUser = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("CUSTOMER_USER"));
        String username = authentication.getName();

        PageDTO pageDTO;
        try {
            if (isCustomerUser) {
                pageDTO = orderService.getOrdersByPageForUser(page, username);
                log.info("Customer user '{}' accessed their orders page with page number: {}", username, page);
            } else {
                pageDTO = orderService.getOrdersByPage(page);
                log.info("User '{}' with higher privileges accessed orders page with page number: {}", username, page);
            }
            model.addAttribute("pageDTO", pageDTO);
        } catch (ServiceException exception) {
            log.error("Error fetching orders for user '{}' on page {}: {}", username, page, exception.getMessage());
            throw new RuntimeException("Error fetching orders: " + exception.getMessage(), exception);
        }

        return "orders";
    }

    @GetMapping(value = {SELLER_PATH + ORDERS_PATH + "/{id}",
            ADMIN_PATH + ORDERS_PATH + "/{id}"})
    public String getOrderById(Model model, @PathVariable("id") Long id) throws ServiceException {
        try {
            OrderDTO order = orderService.getOrderById(id);
            model.addAttribute("order", order);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String role = authentication.getAuthorities().stream()
                    .findFirst()
                    .orElseThrow(() -> new ServiceException("User has no roles"))
                    .getAuthority();
            String username = authentication.getName();

            log.info("Order with id {} was accessed by user {} with role {}", id, username, role);

            return "order";
        } catch (ServiceException exception) {
            log.error("Error fetching order with id {} by user '{}': {}",
                    id, SecurityContextHolder.getContext().getAuthentication().getName(), exception.getMessage());
            throw exception;
        }
    }

    @PostMapping(value = {SELLER_PATH + ORDERS_PATH + "/{id}/change-order-status",
            ADMIN_PATH + ORDERS_PATH + "/{id}/change-order-status"})
    public String changeStatus(@RequestParam("status") String status,
                               @PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMINISTRATOR"))) {
                orderService.changeStatusById(status, id);
                log.info("Administrator '{}' changed the status of order {} to {}", username, id, status);
                return "redirect:" + ADMIN_PATH + ORDERS_PATH + "/" + id;
            } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("SALE_USER"))) {
                orderService.changeStatusById(status, id);
                log.info("Sale user '{}' changed the status of order {} to {}", username, id, status);
                return "redirect:" + SELLER_PATH + ORDERS_PATH + "/" + id;
            } else {
                log.warn("Unauthorized attempt to change the status of order {} by user '{}'", id, username);
                return "redirect:" + ACCESS_DENIED_PATH;
            }
        } catch (ServiceException exception) {
            log.error("Error changing status of order {} to {} by user '{}': {}", id, status, username, exception.getMessage());
            return "redirect:/error";
        }
    }
}