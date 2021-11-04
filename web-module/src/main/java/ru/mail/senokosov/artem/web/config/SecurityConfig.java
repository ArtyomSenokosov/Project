package ru.mail.senokosov.artem.web.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import ru.mail.senokosov.artem.web.config.handler.CustomAccessDeniedHandler;
import ru.mail.senokosov.artem.web.config.handler.CustomAuthenticationSuccessHandler;
import ru.mail.senokosov.artem.web.model.RoleDTOEnum;

@Configuration
@RequiredArgsConstructor
@Profile("!test")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final int BCRYPT_STRENGTH = 12;
    private final UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(encoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**")
                .hasAuthority(RoleDTOEnum.ADMINISTRATOR.name())
                .antMatchers("/customer/**")
                .hasAuthority(RoleDTOEnum.CUSTOMER_USER.name())
                .antMatchers("/seller/**")
                .hasAuthority(RoleDTOEnum.SALE_USER.name())
                .antMatchers("/", "/login", "/access-denied")
                .permitAll()
                .and()
                .formLogin()
                .successHandler(authenticationSuccessHandler())
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .and()
                .csrf()
                .disable();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(BCRYPT_STRENGTH);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }
}