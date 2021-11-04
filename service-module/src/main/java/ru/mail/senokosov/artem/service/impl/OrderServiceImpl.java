package ru.mail.senokosov.artem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.mail.senokosov.artem.repository.ItemRepository;
import ru.mail.senokosov.artem.repository.OrderRepository;
import ru.mail.senokosov.artem.repository.OrderStatusRepository;
import ru.mail.senokosov.artem.repository.UserRepository;
import ru.mail.senokosov.artem.repository.model.*;
import ru.mail.senokosov.artem.service.OrderService;
import ru.mail.senokosov.artem.service.converter.OrderConverter;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.ItemDTO;
import ru.mail.senokosov.artem.service.model.OrderDTO;
import ru.mail.senokosov.artem.service.model.PageDTO;
import ru.mail.senokosov.artem.service.model.PaginationResult;
import ru.mail.senokosov.artem.service.model.enums.OrderStatusDTOEnum;
import ru.mail.senokosov.artem.service.util.PaginationUtil;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static ru.mail.senokosov.artem.service.constant.OrderConstant.MAXIMUM_ORDERS_ON_PAGE;
import static ru.mail.senokosov.artem.service.util.SecurityUtil.getAuthentication;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final OrderConverter orderConverter;

    @Override
    @Transactional
    public PageDTO getOrdersByPage(int page) {
        log.debug("Requesting page number {} for orders.", page);
        Long countOrders = orderRepository.getCount();
        return getPageDTO(page, countOrders, null);
    }

    @Override
    @Transactional
    public PageDTO getOrdersByPageForUser(int page, String username) throws ServiceException {
        log.debug("Requesting page number {} for orders of user: {}", page, username);

        User user = userRepository.findUserByEmail(username);
        if (Objects.isNull(user)) {
            String errorMessage = String.format("User not found for email: %s", username);
            log.error(errorMessage);
            throw new ServiceException(errorMessage);
        }

        Long countOrders = orderRepository.getCountByUserId(user.getId());
        return getPageDTO(page, countOrders, user.getId());
    }

    @Override
    @Transactional
    public OrderDTO getOrderById(Long id) throws ServiceException {
        log.debug("Attempting to find order by id: {}", id);

        Order order = orderRepository.findById(id);
        if (Objects.nonNull(order)) {
            log.info("Order with id: {} found and being converted.", id);
            return orderConverter.convert(order);
        } else {
            log.warn("Order with id: {} was not found.", id);
            throw new ServiceException(String.format("Order with id: %s was not found", id));
        }
    }

    @Override
    @Transactional
    public List<OrderDTO> getAllOrders() throws ServiceException {
        log.debug("Requesting all orders.");
        try {
            List<Order> orders = orderRepository.findAll();
            if (orders.isEmpty()) {
                log.info("No orders were found.");
                return Collections.emptyList();
            }

            List<OrderDTO> orderDTOs = orders.stream()
                    .map(orderConverter::convert)
                    .collect(Collectors.toList());

            log.info("Retrieved {} orders.", orderDTOs.size());
            return orderDTOs;
        } catch (Exception exception) {
            log.error("An error occurred while fetching all orders: {}", exception.getMessage());
            throw new ServiceException("An error occurred while fetching all orders.");
        }
    }

    @Override
    @Transactional
    public void addItemInOrder(ItemDTO itemDTO, OrderDTO orderItemDTO) throws ServiceException {
        log.info("Adding item with UUID {} to order", itemDTO.getUuid());

        Authentication authentication = getAuthentication();
        String userName = authentication.getName();
        log.debug("Authenticated user: {}", userName);

        User userByUsername = userRepository.findUserByEmail(userName);
        if (Objects.nonNull(userByUsername)) {
            Item item = itemRepository.findByUuid(itemDTO.getUuid());
            if (Objects.nonNull(item)) {
                String statusName = OrderStatusDTOEnum.NEW.name();
                OrderStatus status = orderStatusRepository.findByStatusName(statusName);
                if (Objects.nonNull(status)) {
                    Order order = getOrder(orderItemDTO, userByUsername, item, status);
                    orderRepository.persist(order);
                    log.info("Order with UUID {} added successfully", order.getUuidOfOrder());
                } else {
                    log.error("Status with name {} not found", statusName);
                    throw new ServiceException(String.format("Status with status name: %s was not found", statusName));
                }
            } else {
                log.error("Item with UUID {} not found", itemDTO.getUuid());
                throw new ServiceException(String.format("Item with uuid: %s was not found", itemDTO.getUuid()));
            }
        } else {
            log.error("User with username {} not found", userName);
            throw new ServiceException(String.format("User with username: %s was not found", userName));
        }
    }

    @Override
    @Transactional
    public void changeStatusById(String status, Long id) throws ServiceException {
        log.debug("Attempting to change status to '{}' for order with id: {}", status, id);

        Order order = orderRepository.findById(id);
        if (Objects.nonNull(order)) {
            OrderStatus statusName = orderStatusRepository.findByStatusName(status);
            if (Objects.nonNull(statusName)) {
                order.setOrderStatus(statusName);
                orderRepository.merge(order);
                log.info("Successfully changed the status of order with id: {} to '{}'", id, status);
            } else {
                log.warn("Status with status name: '{}' was not found", status);
                throw new ServiceException(String.format("Status with status name: %s was not found", status));
            }
        } else {
            log.warn("Order with id: {} was not found", id);
            throw new ServiceException(String.format("Order with id: %s was not found", id));
        }
    }

    private PageDTO getPageDTO(int page, Long countOrders, Long userId) {
        PaginationResult pagination = PaginationUtil.calculatePagination(countOrders, MAXIMUM_ORDERS_ON_PAGE, page);
        List<Order> orders;
        if (Objects.isNull(userId)) {
            orders = orderRepository.findAll(pagination.getStartPosition(), MAXIMUM_ORDERS_ON_PAGE);
        } else {
            orders = orderRepository.findAllByUserId(userId, pagination.getStartPosition(), MAXIMUM_ORDERS_ON_PAGE);
        }

        log.debug("Retrieved {} orders for page {}.", orders.size(), pagination.getCurrentPage());

        List<OrderDTO> orderDTOs = orders.stream()
                .map(orderConverter::convert)
                .collect(Collectors.toList());

        PageDTO pageDTO = new PageDTO();
        pageDTO.setTotalPages(pagination.getTotalPages());
        pageDTO.setCurrentPage((long) pagination.getCurrentPage());
        pageDTO.setOrders(orderDTOs);
        pageDTO.setStartPosition(pagination.getStartPosition());

        log.info("Page {} of orders served with {} orders.", pagination.getCurrentPage(), orderDTOs.size());
        return pageDTO;
    }

    private Order getOrder(OrderDTO orderItemDTO, User userByUsername, Item item, OrderStatus status) {
        log.debug("Creating order for item UUID {} and user {}", item.getUuid(), userByUsername.getEmail());

        Order order = new Order();
        order.setUuidOfOrder(UUID.randomUUID());
        order.setOrderStatus(status);
        order.setItem(item);
        Long numberOfItems = orderItemDTO.getNumberOfItems();
        order.setNumberOfItems(orderItemDTO.getNumberOfItems());
        order.setDateOfCreation(LocalDateTime.now());
        BigDecimal price = item.getPrice();
        BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(numberOfItems));
        order.setTotalPrice(totalPrice);
        order.setUser(userByUsername);

        log.debug("Order created with UUID: {}", order.getUuidOfOrder());
        return order;
    }
}