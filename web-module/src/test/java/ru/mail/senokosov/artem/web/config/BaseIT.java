package ru.mail.senokosov.artem.web.config;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import ru.mail.senokosov.artem.web.SpringMvcApplication;

@SpringBootTest(classes = SpringMvcApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseIT {

    @Autowired
    protected TestRestTemplate testRestTemplate;

    public static MySQLContainer<?> mySQLContainer;

    @BeforeAll
    public static void startContainer() {
        mySQLContainer = new MySQLContainer<>("mysql:8.0")
                .withUsername("test")
                .withPassword("1111")
                .withReuse(true);
        mySQLContainer.start();
    }

    @AfterAll
    public static void stopContainer() {
        if (mySQLContainer != null && mySQLContainer.isRunning()) {
            mySQLContainer.stop();
        }
    }

    @DynamicPropertySource
    public static void setDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }
}