package ru.mail.senokosov.artem.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mail.senokosov.artem.service.ItemService;
import ru.mail.senokosov.artem.service.model.add.AddItemDTO;
import ru.mail.senokosov.artem.service.model.show.ShowItemDTO;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Log4j2
@Controller
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "items/page/{pageNo}")
    public String getItemByPagination(@PathVariable(value = "pageNo") int pageNo,
                                      @RequestParam("sortField") String sortField,
                                      @RequestParam("sortDir") String sortDir,
                                      Model model) {
        Integer pageSize = 10;

        Page<ShowItemDTO> page = itemService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<ShowItemDTO> items = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listItems", items);
        return "items";
    }

    @GetMapping(value = "/customer/item/{id}")
    public String getCustomerItemById(Model model, @PathVariable("id") Long id) {
        ShowItemDTO items = itemService.getItemById(id);
        model.addAttribute("item", items);
        return "customer-item";
    }

    @GetMapping(value = "/seller/item/{id}")
    public String getSellerItemById(Model model, @PathVariable("id") Long id) {
        ShowItemDTO items = itemService.getItemById(id);
        model.addAttribute("item", items);
        return "seller-item";
    }
}
