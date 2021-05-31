package ru.mail.senokosov.artem.web.controller;

import liquibase.pro.packaged.S;
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
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.UserInfoService;
import ru.mail.senokosov.artem.service.UserService;
import ru.mail.senokosov.artem.service.model.UserDTO;
import ru.mail.senokosov.artem.service.model.UserInfoDTO;
import ru.mail.senokosov.artem.service.model.add.AddUserDTO;
import ru.mail.senokosov.artem.service.model.show.ShowArticleDTO;

import javax.validation.Valid;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final UserInfoService userInfoService;

    @GetMapping(value = "/admin/users")
    public String getUsersByPagination(@PathVariable(value = "pageNo") int pageNo,
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
        return "admin-page";
    }

    @GetMapping(value = "/admin/add-user")
    public String addPage(Model model) {
        model.addAttribute("user", new AddUserDTO());
        return "add-user";
    }

    @PostMapping(value = "/admin/add-user")
    public String add(@Valid UserDTO userDTO, BindingResult error) throws SecurityException {
        log.info("addUser{}", userDTO);
        if (error.hasErrors()) {
            log.info("errors:{}", error);
            return "add-user";
        } else {
            userService.addUserAndSendPasswordToEmail(userDTO);
        }
        return "redirect:/admin/users";
    }

    @PostMapping(value = "/admin/users/delete")
    public String deleteUsers(@RequestParam("checkedIds") List<Long> checkedIds) {
        log.info("checkedIds: {}", checkedIds);
        for (Long id : checkedIds) {
            userService.deleteUserById(id);
        }
        return "redirect:/admin/users";
    }

    @GetMapping(value = "/admin/reset-password/{id}")
    public String resetPasswordById(@PathVariable Long id) {
        userService.resetPasswordAndSendToEmail(id);
        return "redirect:/admin/users";
    }

    @PostMapping(value = "/admin/change-role/{id}")
    public String changeRole(@RequestParam("roleName") UserDTO userDTO,
                             @PathVariable Long id) {
        userService.changeRoleById(userDTO, id);
        return "redirect:/admin/users";
    }

    @GetMapping(value = "customer/user-profile")
    public String getUserProfile(Model model, @RequestParam(value = "username") String userName) {
        UserInfoDTO userInfoDTO = userInfoService.getUserByUserName(userName);
        model.addAttribute("userInfoDTO", userInfoDTO);
        return "user-profile";
    }

    @PostMapping(value = "/customer/change-name")
    public String changeNameById(@Valid UserInfoDTO userInfoDTO, BindingResult result) {
        log.info("userInfoDTO: {}", userInfoDTO);
        if (result.hasErrors()) {
            log.info("errors:{}", result);
            return "user-profile";
        }
        log.info("UserInfoDTO: {}", userInfoDTO.getId());
        log.info("newFirstName: {}", userInfoDTO.getFirstName());
        userInfoService.changeNameById(userInfoDTO);
        return "redirect:/customer/welcome-customer";
    }

    @PostMapping(value = "/customer/change-surname")
    public String changeSurnameById(@Valid UserInfoDTO userInfoDTO, BindingResult result) {
        log.info("userInfoDTO: {}", userInfoDTO);
        if (result.hasErrors()) {
            log.info("errors:{}", result);
            return "user-profile";
        }
        log.info("UserInfoDTO: {}", userInfoDTO.getId());
        log.info("newLastName: {}", userInfoDTO.getLastName());
        userInfoService.changeSurnameById(userInfoDTO);
        return "redirect:/customer/welcome-customer";
    }

    @PostMapping(value = "/customer/change-address")
    public String changeAddressById(@Valid UserInfoDTO userInfoDTO, BindingResult result) {
        log.info("userInfoDTO: {}", userInfoDTO);
        if (result.hasErrors()) {
            log.info("errors:{}", result);
            return "user-profile";
        }
        log.info("id: {}", userInfoDTO.getId());
        log.info("UserInfoDTO: {}", userInfoDTO.getId());
        log.info("newAddress: {}", userInfoDTO.getAddress());
        userInfoService.changeNameById(userInfoDTO);
        return "redirect:/customer/welcome-customer";
    }

    @PostMapping(value = "/customer/change-telephone")
    public String changeTelephoneById(@Valid UserInfoDTO userInfoDTO, BindingResult result) {
        log.info("userInfoDTO: {}", userInfoDTO);
        if (result.hasErrors()) {
            log.info("errors:{}", result);
            return "user-profile";
        }
        log.info("id: {}", userInfoDTO.getId());
        log.info("UserInfoDTO: {}", userInfoDTO.getId());
        log.info("newTelephone: {}", userInfoDTO.getTelephone());
        userInfoService.changeNameById(userInfoDTO);
        return "redirect:/customer/welcome-customer";
    }

    @PostMapping(value = "/customer/change-password")
    public String changePasswordById(@Valid UserInfoDTO userInfoDTO, BindingResult result) {
        log.info("userInfoDTO: {}", userInfoDTO);
        if (result.hasErrors()) {
            log.info("errors:{}", result);
            return "user-profile";
        }
        log.info("id: {}", userInfoDTO.getId());
        log.info("UserInfoDTO: {}", userInfoDTO.getId());
        log.info("oldPassword: {}", userInfoDTO.getOldPassword());
        log.info("newPassword: {}", userInfoDTO.getNewPassword());
        userInfoService.changePasswordById(userInfoDTO);
        return "redirect:/customer/welcome-customer";
    }
}
