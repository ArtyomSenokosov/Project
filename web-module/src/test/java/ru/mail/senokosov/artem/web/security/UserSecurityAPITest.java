package ru.mail.senokosov.artem.web.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.mail.senokosov.artem.service.UserService;
import ru.mail.senokosov.artem.web.config.TestUserDetailsServiceConfig;
import ru.mail.senokosov.artem.web.controller.api.UserAPIController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.mail.senokosov.artem.web.constant.PathConstant.REST_API_USER_PATH;
import static ru.mail.senokosov.artem.web.constant.PathConstant.USERS_PATH;

@ActiveProfiles("security")
@WebMvcTest(controllers = UserAPIController.class,
        excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class)
@Import(TestUserDetailsServiceConfig.class)
public class UserSecurityAPITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void shouldUserWithRoleRestAPIHasAccessToAddUser() throws Exception {
        mockMvc.perform(
                post(REST_API_USER_PATH + USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("rest@gmail.com", "test"))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldUserWithAdminRoleHasNotAccessDeniedToAddUser() throws Exception {
        mockMvc.perform(
                get(REST_API_USER_PATH + USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin@gmail.com", "test"))
        ).andExpect(status().isForbidden());
    }

    @Test
    void shouldUserWithSellerRoleHasNotAccessDeniedToAddUser() throws Exception {
        mockMvc.perform(
                get(REST_API_USER_PATH + USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("seller@gmail.com", "test"))
        ).andExpect(status().isForbidden());
    }

    @Test
    void shouldUserWithCustomerRoleHasNotAccessDeniedToAddUser() throws Exception {
        mockMvc.perform(
                get(REST_API_USER_PATH + USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("customer@gmail.com", "test"))
        ).andExpect(status().isForbidden());
    }
}