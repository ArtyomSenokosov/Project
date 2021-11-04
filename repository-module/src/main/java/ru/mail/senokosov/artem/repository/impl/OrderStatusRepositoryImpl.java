package ru.mail.senokosov.artem.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.OrderStatusRepository;
import ru.mail.senokosov.artem.repository.model.OrderStatus;

import javax.persistence.NoResultException;
import javax.persistence.Query;

@Slf4j
@Repository
public class OrderStatusRepositoryImpl extends GenericRepositoryImpl<Long, OrderStatus> implements OrderStatusRepository {

    @Override
    public OrderStatus findByStatusName(String status) {
        log.debug("Finding OrderStatus by status name: {}", status);
        String stringQuery = "SELECT os FROM OrderStatus as os WHERE os.status=:status";
        Query query = entityManager.createQuery(stringQuery);
        query.setParameter("status", status);
        OrderStatus orderStatus = null;
        try {
            orderStatus = (OrderStatus) query.getSingleResult();
            log.debug("OrderStatus found: {}", orderStatus);
        } catch (NoResultException exception) {
            log.error("No OrderStatus found with status: {}", status, exception);
        } catch (Exception exception) {
            log.error("An error occurred while finding OrderStatus by status name: {}", status, exception);
            throw exception;
        }
        return orderStatus;
    }
}