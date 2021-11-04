package ru.mail.senokosov.artem.web.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan("ru.mail.senokosov.artem.repository.model")
public class PersistenceConfig {
}