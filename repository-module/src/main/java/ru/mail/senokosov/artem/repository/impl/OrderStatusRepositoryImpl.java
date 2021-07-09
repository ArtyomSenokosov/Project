package ru.mail.senokosov.artem.repository.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.OrderStatusRepository;
import ru.mail.senokosov.artem.repository.model.OrderStatus;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import static ru.mail.senokosov.artem.repository.constant.RepositoryConstants.ID_PARAMETER;

@Repository
@Log4j2
public class OrderStatusRepositoryImpl extends GenericRepositoryImpl<Long, OrderStatus> implements OrderStatusRepository {

    @Override
    public OrderStatus findByStatusName(String status) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<OrderStatus> orderStatusQuery = criteriaBuilder.createQuery(OrderStatus.class);
            Root<OrderStatus> orderStatusRoot = orderStatusQuery.from(OrderStatus.class);
            orderStatusQuery.select(orderStatusRoot);
            ParameterExpression<Long> parameter = criteriaBuilder.parameter(Long.class);
            orderStatusQuery.where(criteriaBuilder.equal(orderStatusRoot.get(ID_PARAMETER), parameter));
            TypedQuery<OrderStatus> typedQuery = entityManager.createQuery(orderStatusQuery);
            typedQuery.setParameter(String.valueOf(parameter), status);
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}