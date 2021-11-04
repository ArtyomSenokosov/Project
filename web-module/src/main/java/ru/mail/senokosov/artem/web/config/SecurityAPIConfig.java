package ru.mail.senokosov.artem.web.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.mail.senokosov.artem.service.model.enums.RoleDTOEnum;
import ru.mail.senokosov.artem.web.config.jwt.JwtAuthenticationFilter;
import ru.mail.senokosov.artem.web.config.jwt.JwtTokenFilter;

import static ru.mail.senokosov.artem.web.constant.PathConstant.REST_API_USER_PATH;

@Configuration
@Order(1)
@Profile("!test")
@RequiredArgsConstructor
public class SecurityAPIConfig {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean(name = "apiSecurityFilterChain")
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManagerBean(authenticationConfiguration));
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManagerBean(authenticationConfiguration));

        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .antMatcher(REST_API_USER_PATH + "/**")
                .authorizeRequests()
                .anyRequest().hasAuthority(RoleDTOEnum.SECURE_API_USER.name())
                .and()
                .addFilter(jwtAuthenticationFilter)
                .addFilterBefore(new JwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}