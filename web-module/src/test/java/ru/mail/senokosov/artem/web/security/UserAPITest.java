package ru.mail.senokosov.artem.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.mail.senokosov.artem.service.UserService;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.UserDTO;
import ru.mail.senokosov.artem.web.config.TestSecurityConfig;
import ru.mail.senokosov.artem.web.constant.PathConstant;
import ru.mail.senokosov.artem.web.controller.api.UserAPIController;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserAPIController.class)
@Import(TestSecurityConfig.class)
public class UserAPITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(authorities = "SECURE_API_USER")
    void shouldReturnCreatedWhenUserAddedWithValidData() throws Exception {
        UserDTO newUser = new UserDTO();
        newUser.setEmail("test@example.com");
        newUser.setPassword("password");
        newUser.setFirstName("John");
        newUser.setLastName("Doe");

        when(userService.addUser(any(UserDTO.class))).thenReturn(newUser);

        mockMvc.perform(post(PathConstant.REST_API_USER_PATH + PathConstant.USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(newUser.getEmail()));
    }

    @Test
    @WithMockUser(authorities = "SECURE_API_USER")
    void shouldReturnBadRequestWhenUserAddedWithInvalidData() throws Exception {
        UserDTO invalidUser = new UserDTO();
        invalidUser.setEmail("");
        invalidUser.setPassword("");

        mockMvc.perform(post(PathConstant.REST_API_USER_PATH + PathConstant.USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    @WithMockUser(authorities = "SECURE_API_USER")
    void shouldReturnBadRequestWhenUserServiceThrowsException() throws Exception {
        UserDTO newUser = new UserDTO();
        newUser.setEmail("test@example.com");
        newUser.setPassword("password");
        newUser.setFirstName("John");
        newUser.setLastName("Doe");
        when(userService.addUser(any(UserDTO.class))).thenThrow(new ServiceException("User already exists"));

        mockMvc.perform(post(PathConstant.REST_API_USER_PATH + PathConstant.USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("User already exists"));
    }
}