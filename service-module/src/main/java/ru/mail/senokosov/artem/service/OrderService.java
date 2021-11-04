package ru.mail.senokosov.artem.service;

import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.ItemDTO;
import ru.mail.senokosov.artem.service.model.OrderDTO;
import ru.mail.senokosov.artem.service.model.PageDTO;

import java.util.List;

public interface OrderService {

    PageDTO getOrdersByPage(int page);

    PageDTO getOrdersByPageForUser(int page, String username) throws ServiceException;

    OrderDTO getOrderById(Long id) throws ServiceException;

    List<OrderDTO> getAllOrders() throws ServiceException;

    void addItemInOrder(ItemDTO itemDTO, OrderDTO orderItemDTO) throws ServiceException;

    void changeStatusById(String status, Long id) throws ServiceException;
}