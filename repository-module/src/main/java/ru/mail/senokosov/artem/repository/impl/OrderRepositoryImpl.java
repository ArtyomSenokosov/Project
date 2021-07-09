package ru.mail.senokosov.artem.repository.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.OrderRepository;
import ru.mail.senokosov.artem.repository.model.Order;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import static ru.mail.senokosov.artem.repository.constant.RepositoryConstants.*;

@Repository
@Log4j2
public class OrderRepositoryImpl extends GenericRepositoryImpl<Long, Order> implements OrderRepository {

    @Override
    public Long getCountOrders() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(Order.class)));
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Order> findAll(Integer startPosition, int maximumOrdersOnPage) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> orderQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> orderRoot = orderQuery.from(Order.class);
        CriteriaQuery<Order> select = orderQuery.select(orderRoot)
                .orderBy(criteriaBuilder.asc(orderRoot.get(DATE_PARAMETER)));
        TypedQuery<Order> typedQuery = entityManager.createQuery(select);
        typedQuery.setFirstResult(startPosition);
        typedQuery.setMaxResults(maximumOrdersOnPage);
        return typedQuery.getResultList();
    }
}