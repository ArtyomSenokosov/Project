package ru.mail.senokosov.artem.web;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.mail.senokosov.artem.web.model.enums.RoleDTOEnum;

import static ru.mail.senokosov.artem.web.constant.PathConstant.REST_API_USER_PATH;

@Configuration
@Order(1)
@Profile("!test")
@RequiredArgsConstructor
public class SecurityAPIConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher(REST_API_USER_PATH + "/**")
                .authorizeRequests((authorizeRequests) ->
                        authorizeRequests
                                .anyRequest()
                                .hasAuthority(RoleDTOEnum.SECURE_API_USER.name())
                )
                .httpBasic()
                .and()
                .csrf()
                .disable();
    }
}