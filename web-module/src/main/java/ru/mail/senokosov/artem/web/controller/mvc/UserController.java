package ru.mail.senokosov.artem.web.controller.mvc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mail.senokosov.artem.service.UserService;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.PageDTO;
import ru.mail.senokosov.artem.service.model.UserDTO;
import ru.mail.senokosov.artem.service.model.UserInfoDTO;
import ru.mail.senokosov.artem.service.util.ValidationUtil;

import javax.validation.Valid;

import java.util.List;

import static ru.mail.senokosov.artem.web.constant.PathConstant.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = ADMIN_PATH + USERS_PATH)
    public String getUsersByPagination(Model model, @RequestParam(name = "page", defaultValue = "1") int page) {
        try {
            PageDTO pageDTO = userService.getUsersByPage(page);
            model.addAttribute("pageDTO", pageDTO);
            log.info("Accessing users page with page number: {}", page);
            return "users";
        } catch (Exception exception) {
            log.error("Error accessing users page with page number {}: {}", page, exception.getMessage());
            return "redirect:/error";
        }
    }

    @GetMapping(value = ADMIN_PATH + USERS_PATH + "/add-user")
    public String showAddUserForm(Model model) {
        log.info("Accessing the add user form");
        model.addAttribute("user", new UserDTO());
        model.addAttribute("fieldConstraints", ValidationUtil.getFieldSizeConstraints(UserDTO.class));
        return "add-user";
    }

    @PostMapping(value = ADMIN_PATH + USERS_PATH + "/add-user")
    public String addUser(@ModelAttribute("user") @Valid UserDTO userDTO, BindingResult result) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String adminUsername = authentication.getName();

        if (result.hasErrors()) {
            log.warn("Admin '{}' encountered validation errors while adding a new user", adminUsername);
            return "add-user";
        }

        try {
            userService.addUser(userDTO);
            log.info("Admin '{}' added a new user successfully", adminUsername);
            return "redirect:" + ADMIN_PATH + USERS_PATH;
        } catch (ServiceException exception) {
            log.error("Admin '{}' encountered an error while adding a new user: {}", adminUsername, exception.getMessage());
            return "add-user";
        }
    }

    @PostMapping(value = ADMIN_PATH + USERS_PATH + "/delete-select")
    public String deleteSelectedUsers(@RequestParam List<Long> userIds) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String adminUsername = authentication.getName();

        try {
            for (Long userId : userIds) {
                userService.isDeleteById(userId);
            }
            log.info("Admin '{}' deleted users with IDs {}", adminUsername, userIds);
            return "redirect:" + ADMIN_PATH + USERS_PATH;
        } catch (Exception exception) {
            log.error("Error deleting users by admin '{}': {}", adminUsername, exception.getMessage());
            return "redirect:/error";
        }
    }

    @GetMapping(value = ADMIN_PATH + USERS_PATH + "/{id}/reset-password")
    public String resetPasswordById(@PathVariable Long id) throws ServiceException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String adminUsername = authentication.getName();

        try {
            userService.resetPassword(id);
            log.info("Admin '{}' reset password for user with ID {}", adminUsername, id);
            return "redirect:" + ADMIN_PATH + USERS_PATH;
        } catch (ServiceException exception) {
            log.error("Error resetting password for user with ID {} by admin '{}': {}", id, adminUsername, exception.getMessage());
            throw exception;
        }
    }

    @PostMapping(value = ADMIN_PATH + USERS_PATH + "/{id}/change-role")
    public String changeRoleById(@RequestParam("roleName") String roleName, @PathVariable Long id) throws ServiceException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String adminUsername = authentication.getName();

        try {
            userService.changeRoleById(roleName, id);
            log.info("Admin '{}' changed the role of user with ID {} to '{}'", adminUsername, id, roleName);
            return "redirect:" + ADMIN_PATH + USERS_PATH;
        } catch (ServiceException exception) {
            log.error("Error changing role of user with ID {} to '{}' by admin '{}': {}", id, roleName, adminUsername, exception.getMessage());
            throw exception;
        }
    }

    @GetMapping(value = {"/user-profile"})
    public String getUserProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        UserInfoDTO userInfoDTO;

        try {
            userInfoDTO = userService.getUserByUserName(userName);
            log.info("Successfully retrieved profile for user '{}'", userName);
        } catch (ServiceException exception) {
            log.error("Error retrieving profile for user '{}': {}", userName, exception.getMessage());
            throw new RuntimeException("Error retrieving user profile: " + exception.getMessage(), exception);
        }

        model.addAttribute("userDetails", userInfoDTO);
        if (!model.containsAttribute("addUserDetails")) {
            model.addAttribute("addUserDetails", new UserInfoDTO());
        }
        return "user-profile";
    }

    @PostMapping("/update-profile")
    public String updateUserProfile(@ModelAttribute UserInfoDTO userInfoDTO, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            userService.changeParameterById(userInfoDTO);
            log.info("User '{}' updated their profile successfully", username);
        } catch (ServiceException exception) {
            log.error("Error updating profile for user '{}': {}", username, exception.getMessage());
            model.addAttribute("errorMessage", "Error updating profile: " + exception.getMessage());
            return "user-profile";
        }

        model.addAttribute("successMessage", "Profile updated successfully!");
        return "redirect:/user-profile";
    }
}