package ru.mail.senokosov.artem.web.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.mail.senokosov.artem.service.UserService;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.UserDTO;
import ru.mail.senokosov.artem.web.config.TestSecurityConfig;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.mail.senokosov.artem.web.constant.PathConstant.REST_API_USER_PATH;
import static ru.mail.senokosov.artem.web.constant.PathConstant.USERS_PATH;

@WebMvcTest(controllers = UserAPIController.class)
@WithMockUser
@Import(TestSecurityConfig.class)
class UserAPIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateUserAndReturnStatusCreated() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("user@example.com");
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");

        UserDTO savedUserDTO = new UserDTO();
        savedUserDTO.setId(1L);
        savedUserDTO.setEmail(userDTO.getEmail());
        savedUserDTO.setFirstName(userDTO.getFirstName());
        savedUserDTO.setLastName(userDTO.getLastName());

        when(userService.addUser(any(UserDTO.class))).thenReturn(savedUserDTO);

        mockMvc.perform(post(REST_API_USER_PATH + USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void shouldReturnBadRequestWhenAddingInvalidUser() throws Exception {
        UserDTO userDTO = new UserDTO();

        mockMvc.perform(post(REST_API_USER_PATH + USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWithErrorDetailsWhenAddingInvalidUser() throws Exception {
        UserDTO userDTO = new UserDTO();

        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);
        when(result.getFieldErrors()).thenReturn(List.of(new FieldError("userDTO", "email", "Email cannot be empty")));

        when(userService.addUser(any(UserDTO.class))).thenThrow(new ServiceException("Validation failed"));

        mockMvc.perform(post(REST_API_USER_PATH + USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }
}