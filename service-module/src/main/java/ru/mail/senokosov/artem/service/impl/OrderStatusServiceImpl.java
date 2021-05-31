package ru.mail.senokosov.artem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.mail.senokosov.artem.repository.OrderStatusRepository;
import ru.mail.senokosov.artem.repository.model.OrderStatus;
import ru.mail.senokosov.artem.service.OrderStatusService;
import ru.mail.senokosov.artem.service.converter.OrderStatusConverter;
import ru.mail.senokosov.artem.service.model.show.ShowOrderStatusDTO;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class OrderStatusServiceImpl implements OrderStatusService {

    private final OrderStatusRepository orderStatusRepository;
    private final OrderStatusConverter orderStatusConverter;

    @Override
    @Transactional
    public List<ShowOrderStatusDTO> getAll() {
        List<OrderStatus> orderStatuses = orderStatusRepository.findAll();
        return orderStatuses.stream()
                .map(orderStatusConverter::convert)
                .collect(Collectors.toList());
    }
}
