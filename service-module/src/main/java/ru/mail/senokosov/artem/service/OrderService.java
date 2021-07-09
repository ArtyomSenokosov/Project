package ru.mail.senokosov.artem.service;

import org.hibernate.service.spi.ServiceException;
import ru.mail.senokosov.artem.service.model.OrderItemDTO;
import ru.mail.senokosov.artem.service.model.PageDTO;
import ru.mail.senokosov.artem.service.model.show.ShowItemDTO;
import ru.mail.senokosov.artem.service.model.show.ShowOrderDTO;

import java.util.List;

public interface OrderService {

    List<ShowOrderDTO> getOrders();

    PageDTO getOrdersByPage(int page);

    ShowOrderDTO getOrderById(Long id) throws ServiceException;

    ShowOrderDTO changeStatusById(String status, Long id) throws ServiceException;

    ShowOrderDTO persist(ShowItemDTO showItemDTO, OrderItemDTO orderItemDTO) throws ServiceException;
}