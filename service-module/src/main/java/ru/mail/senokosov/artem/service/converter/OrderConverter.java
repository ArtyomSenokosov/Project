package ru.mail.senokosov.artem.service.converter;

import ru.mail.senokosov.artem.repository.model.Order;
import ru.mail.senokosov.artem.service.model.show.ShowOrderDTO;

public interface OrderConverter {

    ShowOrderDTO convert(Order order);
}