package ru.mail.senokosov.artem.web.config.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

import static ru.mail.senokosov.artem.service.model.enums.RoleDTOEnum.*;
import static ru.mail.senokosov.artem.web.constant.PathConstant.*;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains(ADMINISTRATOR.name())) {
            response.sendRedirect(ADMIN_PATH + USERS_PATH);
        } else if (roles.contains(CUSTOMER_USER.name())) {
            response.sendRedirect(CUSTOMER_PATH + NEWS_PATH);
        } else if (roles.contains(SALE_USER.name())) {
            response.sendRedirect(SELLER_PATH + NEWS_PATH);
        } else if (roles.contains(USER.name())) {
            response.sendRedirect("/user-profile");
        }
    }
}