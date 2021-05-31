package ru.mail.senokosov.artem.service.converter.impl;

import org.springframework.stereotype.Component;
import ru.mail.senokosov.artem.repository.model.OrderStatus;
import ru.mail.senokosov.artem.service.converter.OrderStatusConverter;
import ru.mail.senokosov.artem.service.model.show.ShowOrderStatusDTO;

@Component
public class OrderStatusConverterImpl implements OrderStatusConverter {
    @Override
    public ShowOrderStatusDTO convert(OrderStatus orderStatus) {
        ShowOrderStatusDTO showOrderStatusDTO = new ShowOrderStatusDTO();
        Long id = orderStatus.getId();
        showOrderStatusDTO.setId(id);
        String status = String.valueOf(orderStatus.getOrderStatus());
        showOrderStatusDTO.setStatus(status);
        return showOrderStatusDTO;
    }
}
