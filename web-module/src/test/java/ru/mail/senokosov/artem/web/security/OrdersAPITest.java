package ru.mail.senokosov.artem.web.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.mail.senokosov.artem.service.OrderService;
import ru.mail.senokosov.artem.service.model.OrderDTO;
import ru.mail.senokosov.artem.web.config.TestSecurityConfig;
import ru.mail.senokosov.artem.web.constant.PathConstant;
import ru.mail.senokosov.artem.web.controller.api.OrdersAPIController;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrdersAPIController.class)
@Import(TestSecurityConfig.class)
public class OrdersAPITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

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
    void shouldReturnOkWhenOrdersFetchedWithAuthorization() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        when(orderService.getAllOrders()).thenReturn(Collections.singletonList(orderDTO));

        mockMvc.perform(MockMvcRequestBuilders.get(PathConstant.REST_API_USER_PATH + PathConstant.ORDERS_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void shouldReturnUnauthorizedWhenOrdersFetchedWithoutAuthorization() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PathConstant.REST_API_USER_PATH + PathConstant.ORDERS_PATH))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "SECURE_API_USER")
    void shouldReturnOkWhenOrderFetchedByIdWithAuthorization() throws Exception {
        Long orderId = 1L;
        OrderDTO orderDTO = new OrderDTO();
        when(orderService.getOrderById(orderId)).thenReturn(orderDTO);

        mockMvc.perform(MockMvcRequestBuilders.get(PathConstant.REST_API_USER_PATH + PathConstant.ORDERS_PATH + "/" + orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    @WithAnonymousUser
    void shouldReturnUnauthorizedWhenOrderFetchedByIdWithoutAuthorization() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PathConstant.REST_API_USER_PATH + PathConstant.ORDERS_PATH + "/1"))
                .andExpect(status().isForbidden());
    }
}