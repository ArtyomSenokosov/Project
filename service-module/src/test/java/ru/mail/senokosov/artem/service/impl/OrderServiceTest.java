package ru.mail.senokosov.artem.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.mail.senokosov.artem.repository.ItemRepository;
import ru.mail.senokosov.artem.repository.OrderRepository;
import ru.mail.senokosov.artem.repository.OrderStatusRepository;
import ru.mail.senokosov.artem.repository.UserRepository;
import ru.mail.senokosov.artem.repository.model.Item;
import ru.mail.senokosov.artem.repository.model.Order;
import ru.mail.senokosov.artem.repository.model.OrderStatus;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.converter.OrderConverter;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.ItemDTO;
import ru.mail.senokosov.artem.service.model.OrderDTO;
import ru.mail.senokosov.artem.service.model.PageDTO;
import ru.mail.senokosov.artem.service.model.enums.OrderStatusDTOEnum;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderStatusRepository orderStatusRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private OrderConverter orderConverter;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void shouldReturnCorrectPageInfoWhenGettingOrdersByPage() {
        int page = 1;
        long countOrders = 5L;
        List<Order> mockOrders = IntStream.range(0, (int) countOrders)
                .mapToObj(i -> {
                    Order order = new Order();
                    order.setId((long) i);
                    return order;
                })
                .collect(Collectors.toList());

        when(orderRepository.getCount()).thenReturn(countOrders);
        when(orderRepository.findAll(anyInt(), anyInt())).thenReturn(mockOrders);
        when(orderConverter.convert(any(Order.class))).thenReturn(new OrderDTO());

        PageDTO result = orderService.getOrdersByPage(page);

        assertNotNull(result);
        assertEquals(1, result.getTotalPages());
        assertEquals(countOrders, result.getOrders().size());

        verify(orderRepository, times(1)).getCount();
        verify(orderRepository, times(1)).findAll(anyInt(), anyInt());
    }

    @Test
    void shouldThrowServiceExceptionWhenUserNotFoundOnGettingOrdersByPageForUser() {
        int page = 1;
        String username = "nonexistent@example.com";

        when(userRepository.findUserByEmail(username)).thenReturn(null);

        assertThrows(ServiceException.class,
                () -> orderService.getOrdersByPageForUser(page, username));
    }

    @Test
    void shouldThrowServiceExceptionWhenOrderNotFoundOnGettingOrderById() {
        Long id = 1L;

        when(orderRepository.findById(id)).thenReturn(null);

        assertThrows(ServiceException.class,
                () -> orderService.getOrderById(id));
    }

    @Test
    void shouldThrowServiceExceptionWhenItemNotFoundOnAddingItemInOrder() {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setUuid(UUID.fromString(UUID.randomUUID().toString()));
        OrderDTO orderDTO = new OrderDTO();

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(authentication.getName()).thenReturn("user@example.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findUserByEmail(anyString())).thenReturn(new User());
        when(itemRepository.findByUuid(any(UUID.class))).thenReturn(null);

        assertThrows(ServiceException.class, () -> orderService.addItemInOrder(itemDTO, orderDTO));

        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldThrowServiceExceptionWhenAuthenticatedUserNotFoundOnAddingItemInOrder() {
        ItemDTO itemDTO = new ItemDTO();
        OrderDTO orderDTO = new OrderDTO();
        String userEmail = "nonexistent@example.com";

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(userEmail);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findUserByEmail(userEmail)).thenReturn(null);

        assertThrows(ServiceException.class, () -> orderService.addItemInOrder(itemDTO, orderDTO));

        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldThrowServiceExceptionWhenOrderStatusNotFoundOnAddingItemInOrder() {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setUuid(UUID.fromString(UUID.randomUUID().toString()));
        OrderDTO orderDTO = new OrderDTO();
        String userEmail = "user@example.com";
        User user = new User();

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(authentication.getName()).thenReturn(userEmail);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findUserByEmail(userEmail)).thenReturn(user);
        when(itemRepository.findByUuid(itemDTO.getUuid())).thenReturn(new Item());
        when(orderStatusRepository.findByStatusName(OrderStatusDTOEnum.NEW.name())).thenReturn(null);

        assertThrows(ServiceException.class, () -> orderService.addItemInOrder(itemDTO, orderDTO));

        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldSuccessfullyAddItemInOrder() {
        UUID itemUuid = UUID.randomUUID();
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setUuid(UUID.fromString(itemUuid.toString()));
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setNumberOfItems(1L);

        String userEmail = "user@example.com";
        User user = new User();
        user.setEmail(userEmail);

        Item item = new Item();
        item.setUuid(itemUuid);
        item.setPrice(new BigDecimal("100.00"));

        OrderStatus status = new OrderStatus();
        status.setStatus(OrderStatusDTOEnum.NEW.name());

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(userEmail);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findUserByEmail(userEmail)).thenReturn(user);
        when(itemRepository.findByUuid(itemDTO.getUuid())).thenReturn(item);
        when(orderStatusRepository.findByStatusName(OrderStatusDTOEnum.NEW.name())).thenReturn(status);

        assertDoesNotThrow(() -> orderService.addItemInOrder(itemDTO, orderDTO));

        verify(orderRepository, times(1)).persist(any(Order.class));

        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldThrowServiceExceptionWhenOrderNotFoundOnChangeStatusById() {
        String status = "COMPLETED";
        Long id = 1L;

        when(orderRepository.findById(id)).thenReturn(null);

        assertThrows(ServiceException.class,
                () -> orderService.changeStatusById(status, id));
    }

    @Test
    void shouldThrowServiceExceptionWhenStatusNotFoundOnChangeStatusById() {
        Order order = new Order();
        Long id = 1L;
        String status = "NON_EXISTENT_STATUS";

        when(orderRepository.findById(id)).thenReturn(order);
        when(orderStatusRepository.findByStatusName(status)).thenReturn(null);

        assertThrows(ServiceException.class,
                () -> orderService.changeStatusById(status, id));
    }

    @Test
    void shouldReturnPageInfoWhenUserExistsOnGettingOrdersByPageForUser() throws ServiceException {
        int page = 1;
        String username = "user@example.com";
        User user = new User();
        user.setId(1L);
        when(userRepository.findUserByEmail(username)).thenReturn(user);
        when(orderRepository.getCountByUserId(user.getId())).thenReturn(10L);
        when(orderRepository.findAllByUserId(eq(user.getId()), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(new Order()));
        when(orderConverter.convert(any(Order.class))).thenReturn(new OrderDTO());

        PageDTO result = orderService.getOrdersByPageForUser(page, username);

        assertNotNull(result);
        assertEquals(1, result.getTotalPages());
        assertFalse(result.getOrders().isEmpty());

        verify(orderRepository, times(1)).findAllByUserId(eq(user.getId()), anyInt(), anyInt());
    }

    @Test
    void shouldReturnOrderDTOWhenOrderExistsOnGettingOrderById() throws ServiceException {
        Long id = 1L;
        Order order = new Order();
        when(orderRepository.findById(id)).thenReturn(order);
        when(orderConverter.convert(order)).thenReturn(new OrderDTO());

        OrderDTO result = orderService.getOrderById(id);

        assertNotNull(result);
        verify(orderRepository, times(1)).findById(id);
        verify(orderConverter, times(1)).convert(order);
    }

    @Test
    void shouldReturnListOfOrderDTOsWhenOrdersExistOnGettingAllOrders() throws ServiceException {
        List<Order> mockOrders = Arrays.asList(new Order(), new Order());
        when(orderRepository.findAll()).thenReturn(mockOrders);
        when(orderConverter.convert(any(Order.class))).thenReturn(new OrderDTO());

        List<OrderDTO> result = orderService.getAllOrders();

        assertNotNull(result);
        assertEquals(mockOrders.size(), result.size());
        verify(orderRepository, times(1)).findAll();
        verify(orderConverter, times(mockOrders.size())).convert(any(Order.class));
    }

    @Test
    void shouldReturnEmptyListWhenNoOrdersExistOnGettingAllOrders() throws ServiceException {
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        List<OrderDTO> result = orderService.getAllOrders();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(orderRepository, times(1)).findAll();
        verify(orderConverter, never()).convert(any(Order.class));
    }

    @Test
    void shouldThrowServiceExceptionOnErrorOnGettingAllOrders() {
        when(orderRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        assertThrows(ServiceException.class, () -> orderService.getAllOrders());
        verify(orderRepository, times(1)).findAll();
        verify(orderConverter, never()).convert(any(Order.class));
    }


    @Test
    void shouldChangeStatusSuccessfullyWhenStatusAndOrderExistOnChangeStatusById() {
        Long id = 1L;
        String newStatus = "COMPLETED";
        Order order = new Order();
        OrderStatus status = new OrderStatus();
        status.setStatus(newStatus);

        when(orderRepository.findById(id)).thenReturn(order);
        when(orderStatusRepository.findByStatusName(newStatus)).thenReturn(status);

        assertDoesNotThrow(() -> orderService.changeStatusById(newStatus, id));

        assertEquals(newStatus, order.getOrderStatus().getStatus());
        verify(orderRepository, times(1)).merge(order);
    }
}