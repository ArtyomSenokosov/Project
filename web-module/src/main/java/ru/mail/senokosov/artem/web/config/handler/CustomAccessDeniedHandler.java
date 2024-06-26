package ru.mail.senokosov.artem.web.config.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static ru.mail.senokosov.artem.web.constant.PathConstant.ACCESS_DENIED_PATH;

@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication)) {
            String name = authentication.getName();
            String uri = request.getRequestURI();
            log.info("User {} attempted to access the protected URL: {}", name, uri);
        }
        response.sendRedirect(ACCESS_DENIED_PATH);
    }
}