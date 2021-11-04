package ru.mail.senokosov.artem.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.OrderRepository;
import ru.mail.senokosov.artem.repository.model.Order;

import javax.persistence.Query;
import java.util.List;

@Slf4j
@Repository
public class OrderRepositoryImpl extends GenericRepositoryImpl<Long, Order> implements OrderRepository {

    @Override
    @SuppressWarnings("unchecked")
    public List<Order> findAll(Integer startPosition, int maximumOrdersOnPage) {
        log.debug("Querying all orders with startPosition: {}, maximumOrdersOnPage: {}",
                startPosition, maximumOrdersOnPage);
        String hql = "SELECT o FROM Order as o ORDER BY o.dateOfCreation DESC";
        Query query = entityManager.createQuery(hql);
        query.setFirstResult(startPosition);
        query.setMaxResults(maximumOrdersOnPage);
        List<Order> orders = query.getResultList();
        log.debug("Found {} orders", orders.size());
        return orders;
    }

    @Override
    public Long getCountByUserId(Long userId) {
        log.debug("Getting count of orders for userId: {}", userId);
        String hql = "SELECT COUNT(o) FROM Order o WHERE o.user.id = :userId";
        Query query = entityManager.createQuery(hql);
        query.setParameter("userId", userId);
        Long count = (Long) query.getSingleResult();
        log.debug("Count for userId {}: {}", userId, count);
        return count;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Order> findAllByUserId(Long userId, Integer startPosition, int maximumOrdersOnPage) {
        log.debug("Querying all orders for userId: {}, startPosition: {}, maximumOrdersOnPage: {}",
                userId, startPosition, maximumOrdersOnPage);
        String hql = "SELECT o FROM Order o WHERE o.user.id = :userId ORDER BY o.dateOfCreation DESC";
        Query query = entityManager.createQuery(hql);
        query.setParameter("userId", userId);
        query.setFirstResult(startPosition);
        query.setMaxResults(maximumOrdersOnPage);
        List<Order> orders = query.getResultList();
        log.debug("Found {} orders for userId {}", orders.size(), userId);
        return orders;
    }

    @Override
    public void deleteByItemId(Long itemId) {
        log.debug("Deleting orders related to itemId: {}", itemId);
        String hql = "DELETE FROM Order o WHERE o.item.id = :itemId";
        Query query = entityManager.createQuery(hql);
        query.setParameter("itemId", itemId);
        int deletedCount = query.executeUpdate();
        log.info("Deleted {} orders related to itemId: {}", deletedCount, itemId);
    }
}