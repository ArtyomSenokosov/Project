package ru.mail.senokosov.artem.web.controller.mvc;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.mail.senokosov.artem.service.MailService;
import ru.mail.senokosov.artem.service.UserService;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.PageDTO;
import ru.mail.senokosov.artem.service.model.add.AddUserDTO;
import ru.mail.senokosov.artem.service.model.add.AddUserInfoDTO;
import ru.mail.senokosov.artem.service.model.show.ShowUserDTO;
import ru.mail.senokosov.artem.service.model.show.ShowUserInfoDTO;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

import static ru.mail.senokosov.artem.service.util.SecurityUtil.getAuthentication;
import static ru.mail.senokosov.artem.web.constant.PathConstant.*;

@Log4j2
@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final MailService mailService;

    @GetMapping(value = ADMIN_PATH + USERS_PATH)
    public String getUsersByPagination(Model model, @RequestParam(name = "page", defaultValue = "1") int page) {
        PageDTO pageDTO = userService.getUsersByPage(page);
        model.addAttribute("pageDTO", pageDTO);
        return "users";
    }

    @GetMapping(value = ADMIN_PATH + USER_ADD_PATH)
    public String addPage(Model model) {
        model.addAttribute("addUserDTO", new AddUserDTO());
        return "add-user";
    }

    @PostMapping(value = ADMIN_PATH + USER_ADD_PATH)
    public String add(@Valid AddUserDTO addUserDTO, BindingResult error) throws ServiceException {
        if (error.hasErrors()) {
            return "add-user";
        } else {
            ShowUserDTO userDTO = userService.persist(addUserDTO);
            mailService.sendPasswordToEmailAfterAddUser(userDTO);
        }
        return "redirect:/admin/users";
    }

    @PostMapping(value = ADMIN_PATH + USERS_PATH + "/delete")
    public String deleteUsers(@RequestParam(name = "checkedIds", required = false) List<Long> checkedIds) {
        if (Objects.nonNull(checkedIds)) {
            for (Long id : checkedIds) {
                userService.isDeleteById(id);
            }
        }
        return "redirect:/admin/users";
    }

    @GetMapping(value = ADMIN_PATH + USERS_PATH + "/{id}/reset-password")
    public String resetPasswordById(@PathVariable Long id) throws ServiceException {
        ShowUserDTO userDTO = userService.resetPassword(id);
        mailService.sendPasswordToEmailAfterResetPassword(userDTO);
        return "redirect:/admin/users";
    }

    @PostMapping(value = ADMIN_PATH + USERS_PATH + "/{id}/change-role")
    public String changeRole(@RequestParam("roleName") String roleName,
                             @PathVariable Long id) throws ServiceException {
        userService.changeRoleById(roleName, id);
        return "redirect:/admin/users";
    }

    @GetMapping(value = {CUSTOMER_PATH + USER_PROFILE_PATH,
            ADMIN_PATH + USER_PROFILE_PATH,
            SELLER_PATH + USER_PROFILE_PATH})
    public String getUserProfile(Model model) throws ServiceException {
        Authentication authentication = getAuthentication();
        String userName = authentication.getName();
        ShowUserInfoDTO showUserInfoDTO = userService.getUserByUserName(userName);
        model.addAttribute("userDetails", showUserInfoDTO);
        if (!model.containsAttribute("addUserDetails")) {
            model.addAttribute("addUserDetails", new AddUserInfoDTO());
        }
        return "user-profile";
    }

    @PostMapping(value = CUSTOMER_PATH + USERS_PATH + "/{id}/change-parameter")
    public String changeParameterById(@Valid @ModelAttribute("addUserDetails") AddUserInfoDTO addUserInfoDTO,
                                      BindingResult result,
                                      RedirectAttributes redirectAttributes) throws ServiceException {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addUserDetails", result);
            redirectAttributes.addFlashAttribute("addUserDetails", addUserInfoDTO);
            return "redirect:/customer/user-profile";
        }
        userService.changeParameterById(addUserInfoDTO);
        return "redirect:/customer/user-profile";
    }
}