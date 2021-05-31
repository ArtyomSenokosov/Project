package ru.mail.senokosov.artem.service;

import org.hibernate.service.spi.ServiceException;
import org.springframework.data.domain.Page;
import ru.mail.senokosov.artem.service.model.add.AddOrderDTO;
import ru.mail.senokosov.artem.service.model.OrderDTO;
import ru.mail.senokosov.artem.service.model.show.ShowOrderDTO;

import java.util.List;

public interface OrderService {

    Page<ShowOrderDTO> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

    List<ShowOrderDTO> getAllOrders();

    ShowOrderDTO getOrderById(Long id);

    void persist(AddOrderDTO addOrderDTO);

    OrderDTO changeStatusById(OrderDTO orderDTO) throws ServiceException;
}
