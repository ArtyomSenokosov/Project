package ru.mail.senokosov.artem.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.mail.senokosov.artem.web.config.handler.ApplicationSuccessHandler;

@Configuration
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    public ApplicationSecurity(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(encoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**")
                .hasRole("ADMINISTRATOR")
                .antMatchers("/sale/**")
                .hasRole("SALE_USER")
                .antMatchers("/customer/**")
                .hasRole("CUSTOMER_USER")
                .antMatchers("/secure/**")
                .hasRole("SECURE_API_USER")
                .antMatchers("/login", "/", "/denied")
                .permitAll()
                .and()
                .formLogin()
                .successHandler(new ApplicationSuccessHandler())
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/denied")
                .and()
                .csrf()
                .disable();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}