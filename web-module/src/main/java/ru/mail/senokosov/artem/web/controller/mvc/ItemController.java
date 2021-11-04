package ru.mail.senokosov.artem.web.controller.mvc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mail.senokosov.artem.service.ItemService;
import ru.mail.senokosov.artem.service.OrderService;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.ItemDTO;
import ru.mail.senokosov.artem.service.model.OrderDTO;
import ru.mail.senokosov.artem.service.model.PageDTO;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static ru.mail.senokosov.artem.web.constant.PathConstant.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final OrderService orderService;

    @GetMapping(value = {ADMIN_PATH + ITEMS_PATH,
            SELLER_PATH + ITEMS_PATH,
            CUSTOMER_PATH + ITEMS_PATH})
    public String getItemsByPagination(Model model, @RequestParam(value = "page", defaultValue = "1") int page,
                                       HttpServletRequest request) {
        PageDTO pageDTO = itemService.getItemsByPage(page);
        model.addAttribute("pageDTO", pageDTO);

        String basePath = request.getRequestURI().contains(ADMIN_PATH) ? ADMIN_PATH :
                request.getRequestURI().contains(SELLER_PATH) ? SELLER_PATH :
                        request.getRequestURI().contains(CUSTOMER_PATH) ? CUSTOMER_PATH : "";
        model.addAttribute("basePath", basePath + ITEMS_PATH);

        log.info("Accessing items page: {} with page number: {}", basePath, page);

        return "items";
    }

    @GetMapping(value = {ADMIN_PATH + ITEMS_PATH + "/{id}",
            SELLER_PATH + ITEMS_PATH + "/{id}",
            CUSTOMER_PATH + ITEMS_PATH + "/{id}"})
    public String getItemById(Model model, @PathVariable("id") Long id) throws ServiceException {
        try {
            ItemDTO item = itemService.getItemById(id);
            model.addAttribute("item", item);

            log.info("Item with id {} was accessed", id);
            return "item";
        } catch (ServiceException exception) {
            log.error("Error fetching item with id {}: {}", id, exception.getMessage());
            throw exception;
        }
    }

    @PostMapping(value = {ADMIN_PATH + ITEMS_PATH + "/delete-item",
            SELLER_PATH + ITEMS_PATH + "/delete-item"})
    public String deleteItem(@RequestParam(name = "itemId") Long itemId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMINISTRATOR"))) {
            itemService.isDeleteById(itemId);
            log.info("Administrator '{}' deleted item with id {}", username, itemId);
            return "redirect:" + ADMIN_PATH + ITEMS_PATH;
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("SALE_USER"))) {
            itemService.isDeleteById(itemId);
            log.info("Sale user '{}' deleted item with id {}", username, itemId);
            return "redirect:" + SELLER_PATH + ITEMS_PATH;
        } else {
            log.warn("Unauthorized attempt to delete item with id {} by user '{}'", itemId, username);
            return "redirect:" + ACCESS_DENIED_PATH;
        }
    }

    @PostMapping(value = {ADMIN_PATH + ITEMS_PATH + "/copy/{id}",
            SELLER_PATH + ITEMS_PATH + "/copy/{id}"})
    public String copyItemById(@PathVariable(name = "id") Long itemId) throws ServiceException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMINISTRATOR"))) {
            itemService.copyItemById(itemId);
            log.info("Administrator '{}' copied item with id {}", username, itemId);
            return "redirect:" + ADMIN_PATH + ITEMS_PATH;
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("SALE_USER"))) {
            itemService.copyItemById(itemId);
            log.info("Sale user '{}' copied item with id {}", username, itemId);
            return "redirect:" + SELLER_PATH + ITEMS_PATH;
        } else {
            log.warn("Unauthorized attempt to copy item with id {} by user '{}'", itemId, username);
            return "redirect:" + ACCESS_DENIED_PATH;
        }
    }

    @PostMapping(value = {ADMIN_PATH + ITEMS_PATH + "/add-to-order",
            CUSTOMER_PATH + ITEMS_PATH + "/add-to-order"})
    public String addOrderItemWithNumberOfItems(@Valid @ModelAttribute("orderItem") OrderDTO orderItemDTO,
                                                BindingResult result,
                                                @RequestParam Long itemId) {
        if (result.hasErrors()) {
            log.warn("Validation errors while adding item {} to the order", itemId);
            return "items";
        }

        try {
            ItemDTO showItemDTO = itemService.getItemById(itemId);
            orderService.addItemInOrder(showItemDTO, orderItemDTO);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMINISTRATOR"))) {
                log.info("Administrator '{}' added item {} to the order", username, itemId);
                return "redirect:" + ADMIN_PATH + ITEMS_PATH;
            } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("CUSTOMER_USER"))) {
                log.info("Customer '{}' added item {} to the order", username, itemId);
                return "redirect:" + CUSTOMER_PATH + ITEMS_PATH;
            } else {
                log.warn("Unauthorized attempt to add item {} to the order by user '{}'", itemId, username);
                return "redirect:" + ACCESS_DENIED_PATH;
            }
        } catch (ServiceException exception) {
            log.error("Error adding item {} to the order: {}", itemId, exception.getMessage());
            return "redirect:/error";
        }
    }
}