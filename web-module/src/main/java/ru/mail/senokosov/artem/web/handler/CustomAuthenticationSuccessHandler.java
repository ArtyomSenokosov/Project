package ru.mail.senokosov.artem.web.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import ru.mail.senokosov.artem.web.model.enums.RoleDTOEnum;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

import static ru.mail.senokosov.artem.web.constant.PathConstant.*;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains(RoleDTOEnum.ADMINISTRATOR.name())) {
            response.sendRedirect(ADMIN_PATH + USERS_PATH);
        } else if (roles.contains(RoleDTOEnum.CUSTOMER_USER.name())) {
            response.sendRedirect(CUSTOMER_PATH + ARTICLES_PATH);
        } else if (roles.contains(RoleDTOEnum.SALE_USER.name())) {
            response.sendRedirect(SELLER_PATH + ARTICLES_PATH);
        }
    }
}