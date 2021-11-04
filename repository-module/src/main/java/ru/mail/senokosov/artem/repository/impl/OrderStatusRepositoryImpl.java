package ru.mail.senokosov.artem.repository.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.OrderStatusRepository;
import ru.mail.senokosov.artem.repository.model.OrderStatus;

import javax.persistence.NoResultException;
import javax.persistence.Query;

@Repository
@Log4j2
public class OrderStatusRepositoryImpl extends GenericRepositoryImpl<Long, OrderStatus> implements OrderStatusRepository {

    @Override
    public OrderStatus findByStatusName(String status) {
        String stringQuery = "SELECT os FROM OrderStatus as os WHERE os.status=:status";
        Query query = entityManager.createQuery(stringQuery);
        query.setParameter("status", status);
        try {
            query.getSingleResult();
        } catch (NoResultException e) {
            log.error(e.getMessage(), e);
            return null;
        }
        return (OrderStatus) query.getSingleResult();
    }
}