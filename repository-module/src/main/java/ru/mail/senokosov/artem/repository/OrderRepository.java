package ru.mail.senokosov.artem.repository;

import ru.mail.senokosov.artem.repository.model.Article;
import ru.mail.senokosov.artem.repository.model.Order;

import java.util.List;

public interface OrderRepository extends GenericRepository<Long, Order> {

    List<Order> findOrderByUserId(Long id);

    Long getCountArticle();

    List<Order> findAll(int pageNumber, int pageSize);
}
