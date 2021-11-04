package ru.mail.senokosov.artem.repository.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.ItemRepository;
import ru.mail.senokosov.artem.repository.model.Item;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.UUID;

@Repository
@Log4j2
public class ItemRepositoryImpl extends GenericRepositoryImpl<Long, Item> implements ItemRepository {

    @Override
    public Long getCountItems() {
        String hql = "SELECT COUNT(i.id) FROM Item as i";
        Query query = entityManager.createQuery(hql);
        return (Long) query.getSingleResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Item> findAll(Integer startPosition, int maximumItemsOnPage) {
        String hql = "SELECT i FROM Item as i ORDER BY i.id ASC";
        Query query = entityManager.createQuery(hql);
        query.setFirstResult(startPosition);
        query.setMaxResults(maximumItemsOnPage);
        return query.getResultList();
    }

    @Override
    public Item findByUuid(UUID uuid) {
        String hql = "SELECT i FROM Item as i WHERE i.uuid=:uuid";
        Query query = entityManager.createQuery(hql);
        query.setParameter("uuid", uuid);
        try {
            query.getSingleResult();
        } catch (NoResultException e) {
            log.error(e.getMessage(), e);
            return null;
        }
        return (Item) query.getSingleResult();
    }
}