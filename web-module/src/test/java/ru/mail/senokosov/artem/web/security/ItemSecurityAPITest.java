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
import ru.mail.senokosov.artem.service.ItemService;
import ru.mail.senokosov.artem.web.config.TestUserDetailsServiceConfig;
import ru.mail.senokosov.artem.web.controller.api.ItemsAPIController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.mail.senokosov.artem.web.constant.PathConstant.ITEMS_PATH;
import static ru.mail.senokosov.artem.web.constant.PathConstant.REST_API_USER_PATH;

@ActiveProfiles("security")
@WebMvcTest(controllers = ItemsAPIController.class,
        excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class)
@Import(TestUserDetailsServiceConfig.class)
public class ItemSecurityAPITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Test
    void shouldUserWithRoleRestAPIHasAccessToGetItems() throws Exception {
        mockMvc.perform(
                get(REST_API_USER_PATH + ITEMS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("rest@gmail.com", "test"))
        ).andExpect(status().isOk());
    }

    @Test
    void shouldUserWithAdminRoleHasNotAccessDeniedToGetItems() throws Exception {
        mockMvc.perform(
                get(REST_API_USER_PATH + ITEMS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin@gmail.com", "test"))
        ).andExpect(status().isForbidden());
    }

    @Test
    void shouldUserWithSellerRoleHasNotAccessDeniedToGetItems() throws Exception {
        mockMvc.perform(
                get(REST_API_USER_PATH + ITEMS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("seller@gmail.com", "test"))
        ).andExpect(status().isForbidden());
    }

    @Test
    void shouldUserWithCustomerRoleHasNotAccessDeniedToGetItems() throws Exception {
        mockMvc.perform(
                get(REST_API_USER_PATH + ITEMS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("customer@gmail.com", "test"))
        ).andExpect(status().isForbidden());
    }

    @Test
    void shouldUserWithRoleRestAPIHasAccessToGetItemById() throws Exception {
        mockMvc.perform(
                get(String.format("%s%s/%d", REST_API_USER_PATH, ITEMS_PATH, 1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("rest@gmail.com", "test"))
        ).andExpect(status().isNotFound());
    }

    @Test
    void shouldUserWithAdminRoleHasNotAccessDeniedToGetItemById() throws Exception {
        mockMvc.perform(
                get(String.format("%s%s/%d", REST_API_USER_PATH, ITEMS_PATH, 1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin@gmail.com", "test"))
        ).andExpect(status().isForbidden());
    }

    @Test
    void shouldUserWithSellerRoleHasNotAccessDeniedToGetItemById() throws Exception {
        mockMvc.perform(
                get(String.format("%s%s/%d", REST_API_USER_PATH, ITEMS_PATH, 1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("seller@gmail.com", "test"))
        ).andExpect(status().isForbidden());
    }

    @Test
    void shouldUserWithCustomerRoleHasNotAccessDeniedToGetItemById() throws Exception {
        mockMvc.perform(
                get(String.format("%s%s/%d", REST_API_USER_PATH, ITEMS_PATH, 1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("customer@gmail.com", "test"))
        ).andExpect(status().isForbidden());
    }

    @Test
    void shouldUserWithRoleRestAPIHasAccessToAddItem() throws Exception {
        mockMvc.perform(
                post(REST_API_USER_PATH + ITEMS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("rest@gmail.com", "test"))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldUserWithAdminRoleHasNotAccessDeniedToAddItem() throws Exception {
        mockMvc.perform(
                post(REST_API_USER_PATH + ITEMS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin@gmail.com", "test"))
        ).andExpect(status().isForbidden());
    }

    @Test
    void shouldUserWithSellerRoleHasNotAccessDeniedToAddItem() throws Exception {
        mockMvc.perform(
                post(REST_API_USER_PATH + ITEMS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("seller@gmail.com", "test"))
        ).andExpect(status().isForbidden());
    }

    @Test
    void shouldUserWithCustomerRoleHasNotAccessDeniedToAddItem() throws Exception {
        mockMvc.perform(
                post(REST_API_USER_PATH + ITEMS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("customer@gmail.com", "test"))
        ).andExpect(status().isForbidden());
    }

    @Test
    void shouldUserWithRoleRestAPIHasAccessToDeleteItemById() throws Exception {
        mockMvc.perform(
                delete(String.format("%s%s/%d", REST_API_USER_PATH, ITEMS_PATH, 1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("rest@gmail.com", "test"))
        ).andExpect(status().isNotFound());
    }

    @Test
    void shouldUserWithAdminRoleHasNotAccessDeniedToDeleteItemById() throws Exception {
        mockMvc.perform(
                delete(String.format("%s%s/%d", REST_API_USER_PATH, ITEMS_PATH, 1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin@gmail.com", "test"))
        ).andExpect(status().isForbidden());
    }

    @Test
    void shouldUserWithSellerRoleHasNotAccessDeniedToDeleteItemById() throws Exception {
        mockMvc.perform(
                delete(String.format("%s%s/%d", REST_API_USER_PATH, ITEMS_PATH, 1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("seller@gmail.com", "test"))
        ).andExpect(status().isForbidden());
    }

    @Test
    void shouldUserWithCustomerRoleHasNotAccessDeniedToDeleteItemById() throws Exception {
        mockMvc.perform(
                delete(String.format("%s%s/%d", REST_API_USER_PATH, ITEMS_PATH, 1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("customer@gmail.com", "test"))
        ).andExpect(status().isForbidden());
    }
}
