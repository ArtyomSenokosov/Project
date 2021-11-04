package ru.mail.senokosov.artem.web.controller.mvc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            switch (statusCode) {
                case 404:
                    log.error("Error 404 - Page not found. Path: {}", request.getRequestURI());
                    model.addAttribute("errorCode", "404");
                    model.addAttribute("errorMessage", "Page not found");
                    return "error-404";
                case 500:
                    log.error("Error 500 - Internal Server Error. Path: {}", request.getRequestURI());
                    model.addAttribute("errorCode", "500");
                    model.addAttribute("errorMessage", "Internal Server Error");
                    return "error-500";
                case 403:
                    log.error("Error 403 - Forbidden. Path: {}", request.getRequestURI());
                    model.addAttribute("errorCode", "403");
                    model.addAttribute("errorMessage", "Access Denied");
                    return "error-403";
                case 401:
                    log.error("Error 401 - Unauthorized. Path: {}", request.getRequestURI());
                    model.addAttribute("errorCode", "401");
                    model.addAttribute("errorMessage", "Unauthorized");
                    return "error-401";
                default:
                    log.error("Unknown error occurred. Status code: {}, Path: {}", statusCode, request.getRequestURI());
                    model.addAttribute("errorCode", "Error");
                    model.addAttribute("errorMessage", "An unexpected error occurred");
                    return "error";
            }
        }
        log.error("Unknown error without status code. Path: {}", request.getRequestURI());
        model.addAttribute("errorMessage", "An unexpected error occurred");
        return "error";
    }
}