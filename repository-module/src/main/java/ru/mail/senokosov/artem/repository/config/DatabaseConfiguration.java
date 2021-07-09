package ru.mail.senokosov.artem.repository.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackages = "ru.mail.senokosov.artem.repository.model")
public class DatabaseConfiguration {
}