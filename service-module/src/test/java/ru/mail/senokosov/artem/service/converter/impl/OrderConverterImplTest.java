package ru.mail.senokosov.artem.service.converter.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import ru.mail.senokosov.artem.repository.model.*;
import ru.mail.senokosov.artem.service.model.OrderDTO;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OrderConverterImplTest {

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private OrderConverterImpl orderConverter;

    private Order order;
    private OrderDTO orderDTO;

    @BeforeEach
    void setUp() {
        UUID uuidOfOrder = UUID.randomUUID();

        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setStatus("Shipped");

        Item item = new Item();
        item.setTitle("Item1");

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");

        order = new Order();
        order.setUuidOfOrder(uuidOfOrder);
        order.setOrderStatus(orderStatus);
        order.setItem(item);
        order.setNumberOfItems(5L);
        order.setTotalPrice(new BigDecimal("99.99"));
        order.setUser(user);

        orderDTO = new OrderDTO();
        orderDTO.setUuidOfOrder(uuidOfOrder);
        orderDTO.setOrderStatus(orderStatus.getStatus());
        orderDTO.setTitle(item.getTitle());
        orderDTO.setNumberOfItems(5L);
        orderDTO.setTotalPrice(new BigDecimal("99.99"));
    }

    @Test
    void shouldCorrectlyConvertOrderToDTOWithAllFieldsSet() {
        when(modelMapper.map(any(Order.class), any())).thenReturn(orderDTO);

        OrderDTO resultDTO = orderConverter.convert(order);

        assertEquals(order.getUuidOfOrder(), resultDTO.getUuidOfOrder());
        assertEquals(order.getOrderStatus().getStatus(), resultDTO.getOrderStatus());
        assertEquals(order.getItem().getTitle(), resultDTO.getTitle());
        assertEquals(order.getNumberOfItems(), resultDTO.getNumberOfItems());
        assertEquals(order.getTotalPrice(), resultDTO.getTotalPrice());
        if (order.getUser() != null) {
            assertEquals(order.getUser().getLastName(), resultDTO.getLastName());
        }
    }

    @Test
    void shouldHandleConversionWithoutUser() {
        order.setUser(null);

        when(modelMapper.map(any(Order.class), any())).thenReturn(orderDTO);

        OrderDTO resultDTO = orderConverter.convert(order);

        assertNull(resultDTO.getLastName(), "LastName should not be set when user is absent");
    }

    @Test
    void shouldSetTelephoneInOrderDTOWhenUserInfoIsPresent() {
        User user = new User();
        UserInfo userInfo = new UserInfo();
        userInfo.setTelephone("123-456-7890");
        user.setUserInfo(userInfo);
        order.setUser(user);

        when(modelMapper.map(any(Order.class), any())).thenReturn(orderDTO);
        OrderDTO resultDTO = orderConverter.convert(order);

        assertEquals("123-456-7890", resultDTO.getTelephone(), "Telephone should be set when UserInfo is present");
    }

    @Test
    void shouldSetTelephoneToNAInOrderDTOWhenUserInfoIsAbsent() {
        User user = new User();
        user.setUserInfo(null);
        order.setUser(user);

        when(modelMapper.map(any(Order.class), any())).thenReturn(orderDTO);
        OrderDTO resultDTO = orderConverter.convert(order);

        assertEquals("N/A", resultDTO.getTelephone(), "Telephone should be set to 'N/A' when UserInfo is absent");
    }
}