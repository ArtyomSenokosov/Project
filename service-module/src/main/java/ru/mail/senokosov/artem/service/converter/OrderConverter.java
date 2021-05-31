package ru.mail.senokosov.artem.service.converter;

import ru.mail.senokosov.artem.repository.model.Order;
import ru.mail.senokosov.artem.service.model.*;
import ru.mail.senokosov.artem.service.model.add.AddOrderDTO;
import ru.mail.senokosov.artem.service.model.show.ShowOrderDTO;

public interface OrderConverter {

    ShowOrderDTO convert(Order order);

    Order convert(AddOrderDTO addOrderDTO);

    OrderDTO convertToChange(Order order);
}
