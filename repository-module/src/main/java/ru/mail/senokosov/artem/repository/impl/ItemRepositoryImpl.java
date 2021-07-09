package ru.mail.senokosov.artem.repository.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.ItemRepository;
import ru.mail.senokosov.artem.repository.model.Item;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;

import static ru.mail.senokosov.artem.repository.constant.RepositoryConstants.*;

@Repository
@Log4j2
public class ItemRepositoryImpl extends GenericRepositoryImpl<Long, Item> implements ItemRepository {

    @Override
    public Long getCountItems() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(Item.class)));
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Item> findAll(Integer startPosition, int maximumItemsOnPage) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> itemQuery = criteriaBuilder.createQuery(Item.class);
        Root<Item> itemRoot = itemQuery.from(Item.class);
        CriteriaQuery<Item> select = itemQuery.select(itemRoot)
                .orderBy(criteriaBuilder.asc(itemRoot.get(ID_PARAMETER)));
        TypedQuery<Item> typedQuery = entityManager.createQuery(select);
        typedQuery.setFirstResult(startPosition);
        typedQuery.setMaxResults(maximumItemsOnPage);
        return typedQuery.getResultList();
    }

    @Override
    public Item findByUuid(UUID uuid) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Item> itemQuery = criteriaBuilder.createQuery(Item.class);
            Root<Item> itemRoot = itemQuery.from(Item.class);
            itemQuery.select(itemRoot);
            ParameterExpression<Long> parameter = criteriaBuilder.parameter(Long.class);
            itemQuery.where(criteriaBuilder.equal(itemRoot.get(ID_PARAMETER), parameter));
            TypedQuery<Item> typedQuery = entityManager.createQuery(itemQuery);
            typedQuery.setParameter(String.valueOf(parameter), uuid);
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}