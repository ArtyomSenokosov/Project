package ru.mail.senokosov.artem.service;

import ru.mail.senokosov.artem.service.model.show.ShowOrderStatusDTO;

import java.util.List;

public interface OrderStatusService {

    List<ShowOrderStatusDTO> getAll();
}
