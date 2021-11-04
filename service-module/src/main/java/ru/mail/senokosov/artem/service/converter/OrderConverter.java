package ru.mail.senokosov.artem.service.converter;

import ru.mail.senokosov.artem.repository.model.Order;
import ru.mail.senokosov.artem.service.model.OrderDTO;

public interface OrderConverter {

    OrderDTO convert(Order order);
}