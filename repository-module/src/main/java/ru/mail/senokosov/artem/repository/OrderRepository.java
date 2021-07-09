package ru.mail.senokosov.artem.repository;

import ru.mail.senokosov.artem.repository.model.Order;

import java.util.List;

public interface OrderRepository extends GenericRepository<Long, Order> {

    Long getCountOrders();

    List<Order> findAll(Integer startPosition, int maximumOrdersOnPage);
}