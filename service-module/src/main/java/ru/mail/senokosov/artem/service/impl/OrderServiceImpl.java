package ru.mail.senokosov.artem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.service.spi.ServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.mail.senokosov.artem.repository.ItemRepository;
import ru.mail.senokosov.artem.repository.OrderRepository;
import ru.mail.senokosov.artem.repository.PagingAndSortingRepository;
import ru.mail.senokosov.artem.repository.UserRepository;
import ru.mail.senokosov.artem.repository.model.*;
import ru.mail.senokosov.artem.service.OrderService;
import ru.mail.senokosov.artem.service.converter.OrderConverter;
import ru.mail.senokosov.artem.service.model.add.AddOrderDTO;
import ru.mail.senokosov.artem.service.model.OrderDTO;
import ru.mail.senokosov.artem.service.model.show.ShowOrderDTO;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderConverter orderConverter;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ThreadLocal<PagingAndSortingRepository> pagingAndSortingRepository = new ThreadLocal<PagingAndSortingRepository>();


    @Override
    @Transactional
    public Page<ShowOrderDTO> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.pagingAndSortingRepository.get().findAll(pageable);
    }

    @Override
    @Transactional
    public List<ShowOrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ShowOrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id);
        return orderConverter.convert(order);
    }

    @Override
    @Transactional
    public void persist(AddOrderDTO addOrderDTO) {
        Order order = orderConverter.convert(addOrderDTO);
        Item item = itemRepository.findById(addOrderDTO.getItemId());
        if (Objects.nonNull(item)) {
            order.setItem(item);
        }
        User user = userRepository.findById(addOrderDTO.getUserId());
        if (Objects.nonNull(user)) {
            order.setUser(user);
        }
        orderRepository.persist(order);
    }

    @Override
    @Transactional
    public OrderDTO changeStatusById(OrderDTO orderDTO) throws ServiceException {
        Order order = orderRepository.findById(orderDTO.getId());
        OrderStatus status = orderDTO.getOrderStatus();
        order.setOrderStatus(status);
        orderRepository.merge(order);
        return orderConverter.convertToChange(order);
    }
}
