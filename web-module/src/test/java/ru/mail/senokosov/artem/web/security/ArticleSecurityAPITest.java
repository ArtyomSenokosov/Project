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
import ru.mail.senokosov.artem.service.ArticleService;
import ru.mail.senokosov.artem.web.config.TestUserDetailsServiceConfig;
import ru.mail.senokosov.artem.web.controller.api.ArticleAPIController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.mail.senokosov.artem.web.constant.PathConstant.ARTICLES_PATH;
import static ru.mail.senokosov.artem.web.constant.PathConstant.REST_API_USER_PATH;

@ActiveProfiles("security")
@WebMvcTest(controllers = ArticleAPIController.class,
        excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class)
@Import(TestUserDetailsServiceConfig.class)
public class ArticleSecurityAPITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService articleService;

    @Test
    void shouldUserWithRoleRestAPIHasAccessToGetArticles() throws Exception {
        mockMvc.perform(
                get(REST_API_USER_PATH + ARTICLES_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("rest@gmail.com", "test"))
        ).andExpect(status().isOk());
    }

    @Test
    void shouldUserWithAdminRoleHasNotAccessDeniedToGetArticle() throws Exception {
        mockMvc.perform(
                get(REST_API_USER_PATH + ARTICLES_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin@gmail.com", "test"))
        ).andExpect(status().isForbidden());
    }

    @Test
    void shouldUserWithSellerRoleHasNotAccessDeniedToGetArticle() throws Exception {
        mockMvc.perform(
                get(REST_API_USER_PATH + ARTICLES_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("seller@gmail.com", "test"))
        ).andExpect(status().isForbidden());
    }

    @Test
    void shouldUserWithCustomerRoleHasNotAccessDeniedToGetArticle() throws Exception {
        mockMvc.perform(
                get(REST_API_USER_PATH + ARTICLES_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("customer@gmail.com", "test"))
        ).andExpect(status().isForbidden());
    }

    @Test
    void shouldUserWithRoleRestAPIHasAccessToGetArticleById() throws Exception {
        mockMvc.perform(
                get(String.format("%s%s/%d", REST_API_USER_PATH, ARTICLES_PATH, 1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("rest@gmail.com", "test"))
        ).andExpect(status().isNotFound());
    }

    @Test
    void shouldUserWithAdminRoleHasNotAccessDeniedToGetArticleById() throws Exception {
        mockMvc.perform(
                get(String.format("%s%s/%d", REST_API_USER_PATH, ARTICLES_PATH, 1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin@gmail.com", "test"))
        ).andExpect(status().isForbidden());
    }

    @Test
    void shouldUserWithSellerRoleHasNotAccessDeniedToGetArticleById() throws Exception {
        mockMvc.perform(
                get(String.format("%s%s/%d", REST_API_USER_PATH, ARTICLES_PATH, 1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("seller@gmail.com", "test"))
        ).andExpect(status().isForbidden());
    }

    @Test
    void shouldUserWithCustomerRoleHasNotAccessDeniedToGetArticleById() throws Exception {
        mockMvc.perform(
                get(String.format("%s%s/%d", REST_API_USER_PATH, ARTICLES_PATH, 1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("customer@gmail.com", "test"))
        ).andExpect(status().isForbidden());
    }

    @Test
    void shouldUserWithRoleRestAPIHasAccessToAddArticle() throws Exception {
        mockMvc.perform(
                post(REST_API_USER_PATH + ARTICLES_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("rest@gmail.com", "test"))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldUserWithAdminRoleHasNotAccessDeniedToAddItem() throws Exception {
        mockMvc.perform(
                post(REST_API_USER_PATH + ARTICLES_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin@gmail.com", "test"))
        ).andExpect(status().isForbidden());
    }

    @Test
    void shouldUserWithSellerRoleHasNotAccessDeniedToAddArticle() throws Exception {
        mockMvc.perform(
                post(REST_API_USER_PATH + ARTICLES_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("seller@gmail.com", "test"))
        ).andExpect(status().isForbidden());
    }

    @Test
    void shouldUserWithCustomerRoleHasNotAccessDeniedToAddArticle() throws Exception {
        mockMvc.perform(
                post(REST_API_USER_PATH + ARTICLES_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("customer@gmail.com", "test"))
        ).andExpect(status().isForbidden());
    }

    @Test
    void shouldUserWithRoleRestAPIHasAccessToDeleteArticleById() throws Exception {
        mockMvc.perform(
                delete(String.format("%s%s/%d", REST_API_USER_PATH, ARTICLES_PATH, 1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("rest@gmail.com", "test"))
        ).andExpect(status().isNotFound());
    }

    @Test
    void shouldUserWithAdminRoleHasNotAccessDeniedToDeleteArticleById() throws Exception {
        mockMvc.perform(
                delete(String.format("%s%s/%d", REST_API_USER_PATH, ARTICLES_PATH, 1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin@gmail.com", "test"))
        ).andExpect(status().isForbidden());
    }

    @Test
    void shouldUserWithSellerRoleHasNotAccessDeniedToDeleteArticleById() throws Exception {
        mockMvc.perform(
                delete(String.format("%s%s/%d", REST_API_USER_PATH, ARTICLES_PATH, 1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("seller@gmail.com", "test"))
        ).andExpect(status().isForbidden());
    }

    @Test
    void shouldUserWithCustomerRoleHasNotAccessDeniedToDeleteArticleById() throws Exception {
        mockMvc.perform(
                delete(String.format("%s%s/%d", REST_API_USER_PATH, ARTICLES_PATH, 1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("customer@gmail.com", "test"))
        ).andExpect(status().isForbidden());
    }
}