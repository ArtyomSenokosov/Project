package ru.mail.senokosov.artem.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
@ComponentScan(basePackages = {"ru.mail.senokosov.artem.repository", "ru.mail.senokosov.artem.service"})
public class ApplicationConfig {

    @Bean
    public Random random() {
        return new Random();
    }
}