package ru.mail.senokosov.artem.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.ItemRepository;
import ru.mail.senokosov.artem.repository.model.Item;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.UUID;

@Slf4j
@Repository
public class ItemRepositoryImpl extends GenericRepositoryImpl<Long, Item> implements ItemRepository {

    @Override
    public Item findByUuid(UUID uuid) {
        log.debug("Finding Item by UUID: {}", uuid);
        String hql = "SELECT i FROM Item as i WHERE i.uuid=:uuid";
        Query query = entityManager.createQuery(hql);
        query.setParameter("uuid", uuid);
        try {
            Item item = (Item) query.getSingleResult();
            log.debug("Found item: {}", item);
            return item;
        } catch (NoResultException e) {
            log.debug("No Item found with UUID: {}", uuid);
            return null;
        } catch (Exception exception) {
            log.error("An error occurred while finding item by UUID: {}", uuid, exception);
            throw exception;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Item> findAll(Integer startPosition, int maximumItemsOnPage) {
        log.debug("Finding all Items with startPosition: {} and maximumItemsOnPage: {}",
                startPosition, maximumItemsOnPage);
        String hql = "SELECT i FROM Item as i ORDER BY i.id ASC";
        Query query = entityManager.createQuery(hql);
        query.setFirstResult(startPosition);
        query.setMaxResults(maximumItemsOnPage);
        List<Item> items = query.getResultList();
        log.debug("Found {} items", items.size());
        return items;
    }
}