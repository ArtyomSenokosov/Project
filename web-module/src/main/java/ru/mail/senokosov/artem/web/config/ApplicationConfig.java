package ru.mail.senokosov.artem.web.config;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.mail.senokosov.artem.service.impl.ItemLoaderServiceImpl;

import java.util.Random;

@Slf4j
@Configuration
@ComponentScan(basePackages = {"ru.mail.senokosov.artem.repository", "ru.mail.senokosov.artem.service"})
public class ApplicationConfig {

    @Value("${items.initial.data.path:classpath:database/items.json}")
    private String initialDataPath;

    @Bean
    public Random random() {
        return new Random();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public CommandLineRunner loadInitialData(ItemLoaderServiceImpl itemLoaderServiceImpl) {
        return args -> {
            try {
                log.info("Starting to load initial items data from {}", initialDataPath);
                itemLoaderServiceImpl.loadItemsFromJson(initialDataPath);
                log.info("Successfully loaded initial items data");
            } catch (Exception exception) {
                log.error("Failed to load initial items data from {}: {}",
                        initialDataPath, exception.getMessage(), exception);
            }
        };
    }
}