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
import ru.mail.senokosov.artem.service.ItemService;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.ItemDTO;
import ru.mail.senokosov.artem.web.config.TestSecurityConfig;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.mail.senokosov.artem.web.constant.PathConstant.ITEMS_PATH;
import static ru.mail.senokosov.artem.web.constant.PathConstant.REST_API_USER_PATH;

@WebMvcTest(controllers = ItemsAPIController.class)
@WithMockUser
@Import(TestSecurityConfig.class)
class ItemsAPIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnNoContentWhenItemListIsEmpty() throws Exception {
        when(itemService.getAllItems()).thenReturn(Collections.emptyList());

        mockMvc.perform(get(REST_API_USER_PATH + ITEMS_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnInternalServerErrorWhenRetrievingItemsFails() throws Exception {
        when(itemService.getAllItems()).thenThrow(RuntimeException.class);

        mockMvc.perform(get(REST_API_USER_PATH + ITEMS_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldReturnItemByIdAndStatusOk() throws Exception {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(1L);
        itemDTO.setUuid(UUID.randomUUID());
        itemDTO.setTitle("Gadget");
        itemDTO.setContent("Latest tech gadget");
        itemDTO.setPrice(new BigDecimal("299.99"));

        when(itemService.getItemById(1L)).thenReturn(itemDTO);

        mockMvc.perform(get(REST_API_USER_PATH + ITEMS_PATH + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Gadget"));
    }

    @Test
    void shouldReturnNotFoundWhenItemByIdDoesNotExist() throws Exception {
        when(itemService.getItemById(anyLong())).thenThrow(new ServiceException("Item not found"));

        mockMvc.perform(get(REST_API_USER_PATH + ITEMS_PATH + "/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateItemAndReturnStatusCreated() throws Exception {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setTitle("New Item");
        itemDTO.setContent("Description of the new item");
        itemDTO.setPrice(new BigDecimal("100.00"));

        ItemDTO savedItemDTO = new ItemDTO();
        savedItemDTO.setId(1L);
        savedItemDTO.setUuid(UUID.randomUUID());
        savedItemDTO.setTitle(itemDTO.getTitle());
        savedItemDTO.setContent(itemDTO.getContent());
        savedItemDTO.setPrice(itemDTO.getPrice());

        when(itemService.addItem(any(ItemDTO.class))).thenReturn(savedItemDTO);

        mockMvc.perform(post(REST_API_USER_PATH + ITEMS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("New Item"));
    }

    @Test
    void shouldReturnBadRequestWhenAddingInvalidItem() throws Exception {
        ItemDTO itemDTO = new ItemDTO();

        mockMvc.perform(post(REST_API_USER_PATH + ITEMS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteItemAndReturnStatusOk() throws Exception {
        when(itemService.isDeleteById(1L)).thenReturn(true);

        mockMvc.perform(delete(REST_API_USER_PATH + ITEMS_PATH + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentItem() throws Exception {
        when(itemService.isDeleteById(anyLong())).thenReturn(false);

        mockMvc.perform(delete(REST_API_USER_PATH + ITEMS_PATH + "/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}