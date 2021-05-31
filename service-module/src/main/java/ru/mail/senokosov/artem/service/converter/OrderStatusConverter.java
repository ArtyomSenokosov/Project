package ru.mail.senokosov.artem.service.converter;

import ru.mail.senokosov.artem.repository.model.OrderStatus;
import ru.mail.senokosov.artem.service.model.show.ShowOrderStatusDTO;

public interface OrderStatusConverter {

    ShowOrderStatusDTO convert(OrderStatus orderStatus);
}
