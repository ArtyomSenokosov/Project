package ru.mail.senokosov.artem.web.controller.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.mail.senokosov.artem.service.OrderService;
import ru.mail.senokosov.artem.service.model.OrderDTO;
import ru.mail.senokosov.artem.web.config.TestSecurityConfig;

import java.util.Collections;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.mail.senokosov.artem.web.constant.PathConstant.ORDERS_PATH;
import static ru.mail.senokosov.artem.web.constant.PathConstant.REST_API_USER_PATH;

@WebMvcTest(controllers = OrdersAPIController.class)
@WithMockUser
@Import(TestSecurityConfig.class)
class OrdersAPIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    void shouldReturnNoContentWhenOrderListIsEmpty() throws Exception {
        when(orderService.getAllOrders()).thenReturn(Collections.emptyList());

        mockMvc.perform(get(REST_API_USER_PATH + ORDERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnInternalServerErrorWhenRetrievingOrdersFails() throws Exception {
        when(orderService.getAllOrders()).thenThrow(RuntimeException.class);

        mockMvc.perform(get(REST_API_USER_PATH + ORDERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldReturnOrderByIdAndStatusOk() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(1L);

        when(orderService.getOrderById(1L)).thenReturn(orderDTO);

        mockMvc.perform(get(REST_API_USER_PATH + ORDERS_PATH + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderDTO.getId()));
    }

    @Test
    void shouldReturnNotFoundWhenOrderByIdDoesNotExist() throws Exception {
        when(orderService.getOrderById(anyLong())).thenReturn(null);

        mockMvc.perform(get(REST_API_USER_PATH + ORDERS_PATH + "/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}