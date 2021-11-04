package ru.mail.senokosov.artem.web.controller.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static ru.mail.senokosov.artem.web.constant.PathConstant.ACCESS_DENIED_PATH;

@Controller
public class AccessDeniedController {

    @GetMapping(ACCESS_DENIED_PATH)
    public String showAccessDenied() {
        return ACCESS_DENIED_PATH;
    }
}