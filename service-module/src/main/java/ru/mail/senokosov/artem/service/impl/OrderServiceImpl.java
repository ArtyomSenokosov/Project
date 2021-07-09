package ru.mail.senokosov.artem.service.impl;

import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.mail.senokosov.artem.repository.ItemRepository;
import ru.mail.senokosov.artem.repository.OrderRepository;
import ru.mail.senokosov.artem.repository.OrderStatusRepository;
import ru.mail.senokosov.artem.repository.UserRepository;
import ru.mail.senokosov.artem.repository.model.*;
import ru.mail.senokosov.artem.service.OrderService;
import ru.mail.senokosov.artem.service.converter.OrderConverter;
import ru.mail.senokosov.artem.service.model.OrderItemDTO;
import ru.mail.senokosov.artem.service.model.enums.OrderStatusDTOEnum;
import ru.mail.senokosov.artem.service.model.PageDTO;
import ru.mail.senokosov.artem.service.model.show.ShowItemDTO;
import ru.mail.senokosov.artem.service.model.show.ShowOrderDTO;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static ru.mail.senokosov.artem.service.constant.OrderPaginateConstant.MAXIMUM_ORDERS_ON_PAGE;
import static ru.mail.senokosov.artem.service.util.SecurityUtil.getAuthentication;
import static ru.mail.senokosov.artem.service.util.ServiceUtil.getPageDTO;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderConverter orderConverter;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final OrderStatusRepository orderStatusRepository;

    @Override
    @Transactional
    public PageDTO getOrdersByPage(int page) {
        Long countOrders = orderRepository.getCountOrders();
        PageDTO pageDTO = getPageDTO(page, countOrders, MAXIMUM_ORDERS_ON_PAGE);
        List<Order> orders = orderRepository.findAll(pageDTO.getStartPosition(), MAXIMUM_ORDERS_ON_PAGE);
        pageDTO.getOrders().addAll(orders.stream()
                .map(orderConverter::convert)
                .collect(Collectors.toList()));
        return pageDTO;
    }

    @Override
    @Transactional
    public ShowOrderDTO getOrderById(Long id) throws ServiceException {
        Order order = orderRepository.findById(id);
        if (Objects.nonNull(order)) {
            return orderConverter.convert(order);
        } else {
            throw new ServiceException(String.format("Order with id: %s was not found", id));
        }
    }

    @Override
    @Transactional
    public ShowOrderDTO changeStatusById(String status, Long id) throws ServiceException {
        Order order = orderRepository.findById(id);
        if (Objects.nonNull(order)) {
            OrderStatus statusName = orderStatusRepository.findByStatusName(status);
            if (Objects.nonNull(statusName)) {
                order.setOrderStatus(statusName);
                return orderConverter.convert(order);
            } else {
                throw new ServiceException(String.format("Status with status name: %s was not found", status));
            }
        } else {
            throw new ServiceException(String.format("Order with id: %s was not found", id));
        }
    }

    @Override
    @Transactional
    public ShowOrderDTO persist(ShowItemDTO showItemDTO, OrderItemDTO orderItemDTO) throws ServiceException {
        Authentication authentication = getAuthentication();
        String userName = authentication.getName();
        User userByUsername = userRepository.findUserByUsername(userName);
        if (Objects.nonNull(userByUsername)) {
            Item item = itemRepository.findByUuid(showItemDTO.getUuid());
            if (Objects.nonNull(item)) {
                String statusName = OrderStatusDTOEnum.NEW.name();
                OrderStatus status = orderStatusRepository.findByStatusName(statusName);
                if (Objects.nonNull(status)) {
                    Order order = getOrder(orderItemDTO, userByUsername, item, status);
                    orderRepository.persist(order);
                    return orderConverter.convert(order);
                } else {
                    throw new ServiceException(String.format("Status with status name: %s was not found", statusName));
                }
            } else {
                throw new ServiceException(String.format("Item with uuid: %s was not found", showItemDTO.getUuid()));
            }
        } else {
            throw new ServiceException(String.format("User with username: %s was not found", userName));
        }
    }

    private Order getOrder(OrderItemDTO orderItemDTO, User userByUsername, Item item, OrderStatus status) {
        Order order = new Order();
        order.setNumberOfOrder(UUID.randomUUID());
        order.setOrderStatus(status);
        order.setItem(item);
        Long numberOfItems = orderItemDTO.getNumberOfItems();
        order.setNumberOfItems(orderItemDTO.getNumberOfItems());
        order.setLocalDateTime(LocalDateTime.now());
        BigDecimal price = item.getPrice();
        BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(numberOfItems));
        order.setTotalPrice(totalPrice);
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUser(userByUsername);
        orderInfo.setOrder(order);
        order.setOrderInfo(orderInfo);
        return order;
    }

    @Override
    @Transactional
    public List<ShowOrderDTO> getOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderConverter::convert)
                .collect(Collectors.toList());
    }
}