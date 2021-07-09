package ru.mail.senokosov.artem.repository;

import ru.mail.senokosov.artem.repository.model.OrderStatus;

public interface OrderStatusRepository extends GenericRepository<Long, OrderStatus> {

    OrderStatus findByStatusName(String status);
}