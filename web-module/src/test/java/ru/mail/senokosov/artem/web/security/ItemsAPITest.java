package ru.mail.senokosov.artem.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.mail.senokosov.artem.service.ItemService;
import ru.mail.senokosov.artem.service.model.ItemDTO;
import ru.mail.senokosov.artem.web.config.TestSecurityConfig;
import ru.mail.senokosov.artem.web.constant.PathConstant;
import ru.mail.senokosov.artem.web.controller.api.ItemsAPIController;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemsAPIController.class)
@Import(TestSecurityConfig.class)
public class ItemsAPITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

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
    void shouldReturnOkWhenItemsFetchedWithAuthorization() throws Exception {
        ItemDTO itemDTO = new ItemDTO();
        when(itemService.getAllItems()).thenReturn(Collections.singletonList(itemDTO));

        mockMvc.perform(MockMvcRequestBuilders.get(PathConstant.REST_API_USER_PATH + PathConstant.ITEMS_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void shouldReturnUnauthorizedWhenItemsFetchedWithoutAuthorization() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PathConstant.REST_API_USER_PATH + PathConstant.ITEMS_PATH))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "SECURE_API_USER")
    void shouldReturnOkWhenItemFetchedByIdWithAuthorization() throws Exception {
        Long itemId = 1L;
        ItemDTO itemDTO = new ItemDTO();
        when(itemService.getItemById(itemId)).thenReturn(itemDTO);

        mockMvc.perform(MockMvcRequestBuilders.get(PathConstant.REST_API_USER_PATH + PathConstant.ITEMS_PATH + "/" + itemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    @WithAnonymousUser
    void shouldReturnUnauthorizedWhenItemFetchedByIdWithoutAuthorization() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PathConstant.REST_API_USER_PATH + PathConstant.ITEMS_PATH + "/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "SECURE_API_USER")
    void shouldReturnCreatedWhenItemAddedWithValidData() throws Exception {
        ItemDTO newItem = new ItemDTO();
        newItem.setTitle("Test Product");
        newItem.setContent("This is a test product.");
        newItem.setPrice(BigDecimal.valueOf(10.99));
        when(itemService.addItem(any(ItemDTO.class))).thenReturn(newItem);

        mockMvc.perform(post(PathConstant.REST_API_USER_PATH + PathConstant.ITEMS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newItem))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(newItem.getTitle()));
    }

    @Test
    @WithMockUser(authorities = "SECURE_API_USER")
    void shouldReturnBadRequestWhenItemAddedWithInvalidData() throws Exception {
        mockMvc.perform(post(PathConstant.REST_API_USER_PATH + PathConstant.ITEMS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    @WithMockUser(authorities = "SECURE_API_USER")
    void shouldReturnOkWhenItemDeletedWithExistingId() throws Exception {
        Long itemId = 1L;
        when(itemService.isDeleteById(itemId)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete(PathConstant.REST_API_USER_PATH + PathConstant.ITEMS_PATH + "/" + itemId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Item with id " + itemId + " was successfully deleted."));
    }

    @Test
    @WithMockUser(authorities = "SECURE_API_USER")
    void shouldReturnNotFoundWhenItemDeletedWithNonExistingId() throws Exception {
        Long itemId = 2L;
        when(itemService.isDeleteById(itemId)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete(PathConstant.REST_API_USER_PATH + PathConstant.ITEMS_PATH + "/" + itemId))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Item with id " + itemId + " was not found."));
    }
}