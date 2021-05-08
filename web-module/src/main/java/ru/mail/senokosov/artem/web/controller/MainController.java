package ru.mail.senokosov.artem.web.controller;


import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.repository.model.enums.RoleEnum;
import ru.mail.senokosov.artem.service.ReviewService;
import ru.mail.senokosov.artem.service.UserService;
import ru.mail.senokosov.artem.service.model.UserDTO;

import java.util.List;

@Controller
public class MainController {

    private final UserService userService;
    private final ReviewService reviewService;

    public MainController(UserService userService, ReviewService reviewService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @GetMapping(value = "/")
    public String viewHomePage(Model model) {
        return findPaginated(1, "firstName", "asc", model);
    }

    @GetMapping(value = "admin/users/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model) {
        Integer pageSize = 10;

        Page<User> page = userService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<User> users = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listUsers", users);
        return "index";
    }

    @PostMapping(value = "admin/users/page/{pageNo}")
    public void deleteFile(List<Long> id, Model model) {
        for (Long i : id) {
            UserDTO userDTO = userService.deleteUser(i);
        }
    }

    @PostMapping(value = "add_file")
    public String addFile(Long id, String secondname, String firstname, String middlename, String email, RoleEnum roleEnum, Model model) {
        UserDTO userDTO = userService.addUser(id, secondname, middlename, email, roleEnum);
        model.addAttribute("user", userDTO);
        return "user";
    }

}
