package ru.mail.senokosov.artem.web.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.mail.senokosov.artem.repository.model.enums.RoleEnum;

@Configuration
@Order(1)
@RequiredArgsConstructor
public class SecurityAPIConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder authentication) throws Exception {
        authentication.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/api/**")
                .authorizeRequests((authorizeRequests) -> authorizeRequests
                        .antMatchers("/users", "/articles/**", "/items", "/orders")
                        .hasAuthority(RoleEnum.SECURE_API_USER.name())
                        .anyRequest()
                        .authenticated())
                .httpBasic()
                .and()
                .csrf()
                .disable();
    }
}
