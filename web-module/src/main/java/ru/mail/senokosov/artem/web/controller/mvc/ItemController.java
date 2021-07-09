package ru.mail.senokosov.artem.web.controller.mvc;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mail.senokosov.artem.service.ItemService;
import ru.mail.senokosov.artem.service.OrderService;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.OrderItemDTO;
import ru.mail.senokosov.artem.service.model.PageDTO;
import ru.mail.senokosov.artem.service.model.show.ShowItemDTO;

import javax.validation.Valid;
import java.util.UUID;

import static ru.mail.senokosov.artem.web.constant.PathConstant.*;

@RequiredArgsConstructor
@Log4j2
@Controller
public class ItemController {

    private final ItemService itemService;
    private final OrderService orderService;

    @GetMapping(value = {CUSTOMER_PATH + ITEMS_PATH,
            SELLER_PATH + ITEMS_PATH,
            ADMIN_PATH + ITEMS_PATH})
    public String getItemsByPagination(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        PageDTO pageDTO = itemService.getItemsByPage(page);
        model.addAttribute("page", pageDTO);
        model.addAttribute("orderItem", new OrderItemDTO());
        return "items";
    }

    @GetMapping(value = {SELLER_PATH + ITEMS_PATH + "/{uuid}",
            CUSTOMER_PATH + ITEMS_PATH + "/{uuid}"})
    public String getItemDetailsByUuid(@PathVariable UUID uuid, Model model) throws ServiceException {
        ShowItemDTO showItemDTO = itemService.getItemByUuid(uuid);
        model.addAttribute("item", showItemDTO);
        return "item";
    }

    @GetMapping(value = SELLER_PATH + ITEMS_PATH + "/{uuid}/delete")
    public String deleteItemByUuid(@PathVariable UUID uuid) throws ServiceException {
        itemService.isDeleteByUuid(uuid);
        return "redirect:/seller/items";
    }

    @GetMapping(value = SELLER_PATH + ITEMS_PATH + "/{uuid}/copy")
    public String copyItemByUuid(@PathVariable UUID uuid) throws ServiceException {
        itemService.CopyItemByUuid(uuid);
        return "redirect:/seller/items";
    }

    @PostMapping(value = "/customer/items/{uuid}/order-item")
    public String addOrderItemWithNumberOfItems(@Valid @ModelAttribute("orderItem") OrderItemDTO orderItemDTO,
                                                BindingResult result,
                                                @PathVariable UUID uuid) throws ServiceException {
        if (result.hasErrors()) {
            return "items";
        }
        ShowItemDTO showItemDTO = itemService.getItemByUuid(uuid);
        orderService.persist(showItemDTO, orderItemDTO);
        return String.format("redirect:%s%s", CUSTOMER_PATH, ITEMS_PATH);
    }
}