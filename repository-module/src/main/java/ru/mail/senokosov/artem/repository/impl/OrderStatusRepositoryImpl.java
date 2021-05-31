package ru.mail.senokosov.artem.repository.impl;

import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.OrderStatusRepository;
import ru.mail.senokosov.artem.repository.model.OrderStatus;

@Repository
public class OrderStatusRepositoryImpl extends GenericRepositoryImpl<Long, OrderStatus> implements OrderStatusRepository {
}
