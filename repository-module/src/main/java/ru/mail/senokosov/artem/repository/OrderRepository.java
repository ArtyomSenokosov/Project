package ru.mail.senokosov.artem.repository;

import ru.mail.senokosov.artem.repository.model.Order;

import java.util.List;

public interface OrderRepository extends GenericRepository<Long, Order> {

    List<Order> findAll(Integer startPosition, int maximumOrdersOnPage);

    Long getCountByUserId(Long userId);

    List<Order> findAllByUserId(Long userId, Integer startPosition, int maximumOrdersOnPage);

    void deleteByItemId(Long itemId);
}