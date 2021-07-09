package ru.mail.senokosov.artem.web.controller.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.mail.senokosov.artem.service.ItemService;
import ru.mail.senokosov.artem.service.model.show.ShowItemDTO;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.mail.senokosov.artem.web.constant.PathConstant.ITEMS_PATH;
import static ru.mail.senokosov.artem.web.constant.PathConstant.REST_API_USER_PATH;


@WebMvcTest(controllers = ItemsAPIController.class,
        excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class)
@ActiveProfiles("test")
class ItemAPIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetItemsAndReturnOk() throws Exception {
        mockMvc.perform(get(REST_API_USER_PATH + ITEMS_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldVerifyThatTheBusinessLogicWasCalledWhenWeRequestGEtItems() throws Exception {
        mockMvc.perform(get(REST_API_USER_PATH + ITEMS_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(itemService, times(1)).getItems();
    }

    @Test
    void shouldReturnCollectionOfItemsWhenWeRequestGEtItems() throws Exception {
        ShowItemDTO showItemDTO = new ShowItemDTO();
        showItemDTO.setId(1L);
        showItemDTO.setUuid(UUID.fromString("de05425c-da35-45ba-be2f-61284704662e"));
        showItemDTO.setTitle("title");
        showItemDTO.setContent("content");
        showItemDTO.setPrice(BigDecimal.valueOf(500));

        List<ShowItemDTO> items = Collections.singletonList(showItemDTO);

        when(itemService.getItems()).thenReturn(items);

        MvcResult mvcResult = mockMvc.perform(get(REST_API_USER_PATH + ITEMS_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).isEqualToIgnoringCase(objectMapper.writeValueAsString(items));
    }

    @Test
    void shouldReturnItemWhenWeRequestGEtItemById() throws Exception {
        ShowItemDTO showItemDTO = new ShowItemDTO();
        Long id = 1L;
        showItemDTO.setId(id);
        showItemDTO.setUuid(UUID.fromString("de05425c-da35-45ba-be2f-61284704662e"));
        showItemDTO.setTitle("title");
        showItemDTO.setContent("content");
        showItemDTO.setPrice(BigDecimal.valueOf(500));

        when(itemService.getItemById(id)).thenReturn(showItemDTO);

        MvcResult mvcResult = mockMvc.perform(get(String.format("%s%s/%s", REST_API_USER_PATH, ITEMS_PATH, id))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).isEqualToIgnoringCase(objectMapper.writeValueAsString(showItemDTO));
    }

    @Test
    void should404WhenWeRequestGEtItemByWrongId() throws Exception {
        ShowItemDTO showItemDTO = new ShowItemDTO();
        Long id = 1L;
        showItemDTO.setId(id);
        showItemDTO.setUuid(UUID.fromString("de05425c-da35-45ba-be2f-61284704662e"));
        showItemDTO.setTitle("title");
        showItemDTO.setContent("content");
        showItemDTO.setPrice(BigDecimal.valueOf(500));

        when(itemService.getItemById(id)).thenReturn(showItemDTO);
        Long wrongId = 2L;

        mockMvc.perform(get(String.format("%s%s/%s", REST_API_USER_PATH, ITEMS_PATH, wrongId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void should201WhenWeRequestAddItem() throws Exception {
        mockMvc.perform(post(REST_API_USER_PATH + ITEMS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"title\": \"Test\",\n" +
                        "  \"price\": \"223123\",\n" +
                        "  \"content\": \"Test for kid\"\n" +
                        "}"))
                .andExpect(status().isCreated());
    }

    @Test
    void should400WhenWeRequestAddItemWithNotValidField() throws Exception {
        mockMvc.perform(post(REST_API_USER_PATH + ITEMS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"title\": \"Test title where number of character more 40\",\n" +
                        "  \"price\": \"223123\",\n" +
                        "  \"content\": \"Test for kid\"\n" +
                        "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should200WhenWeRequestDeleteItemById() throws Exception {
        Long id = 1L;
        when(itemService.isDeleteById(id)).thenReturn(true);
        mockMvc.perform(delete(String.format("%s%s/%s", REST_API_USER_PATH, ITEMS_PATH, id))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void should404WhenWeRequestDeleteItemByWrongId() throws Exception {
        Long id = 1L;
        when(itemService.isDeleteById(id)).thenReturn(false);
        mockMvc.perform(delete(String.format("%s%s/%s", REST_API_USER_PATH, ITEMS_PATH, id))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}