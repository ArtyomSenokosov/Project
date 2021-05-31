package ru.mail.senokosov.artem.repository.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.OrderRepository;
import ru.mail.senokosov.artem.repository.model.Order;
import ru.mail.senokosov.artem.repository.model.User;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;

import static ru.mail.senokosov.artem.repository.constant.RepositoryConstants.*;

@Repository
@Log4j2
public class OrderRepositoryImpl extends GenericRepositoryImpl<Long, Order> implements OrderRepository {

    @Override
    @SuppressWarnings("unchecked")
    public List<Order> findOrderByUserId(Long id) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Order> query = criteriaBuilder.createQuery(Order.class);
            Root<Order> orderRoot = query.from(Order.class);
            query.select(orderRoot);
            ParameterExpression<Long> parameter = criteriaBuilder.parameter(Long.class);
            query.where(criteriaBuilder.equal(orderRoot.get(USER_ID_PARAMETER), parameter));
            TypedQuery<Order> typedQuery = entityManager.createQuery(query);
            typedQuery.setParameter(parameter, id);
            return typedQuery.getResultList();
        } catch (NoResultException e) {
            log.info("Order does not exist");
            return null;
        }
    }

    @Override
    public Long getCountArticle() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(User.class)));
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Order> findAll(int pageNumber, int pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(Order.class)));
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        CriteriaQuery<Order> orderQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> orderRoot = orderQuery.from(Order.class);
        CriteriaQuery<Order> select = orderQuery.select(orderRoot)
                .orderBy(criteriaBuilder.asc(orderRoot.get(DATE_PARAMETER)));
        TypedQuery<Order> typedQuery = entityManager.createQuery(select);
        if (pageSize < count.intValue()) {
            typedQuery.setFirstResult((pageNumber) = pageSize - pageSize);
            typedQuery.setMaxResults(pageSize);
            return typedQuery.getResultList();
        }
        return typedQuery.getResultList();
    }
}