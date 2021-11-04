package ru.mail.senokosov.artem.repository.impl;

import lombok.extern.slf4j.Slf4j;
import ru.mail.senokosov.artem.repository.GenericRepository;
import ru.mail.senokosov.artem.repository.exception.RepositoryException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@Slf4j
public class GenericRepositoryImpl<I, T> implements GenericRepository<I, T> {

    @PersistenceContext
    protected EntityManager entityManager;

    private final Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public GenericRepositoryImpl() {
        log.debug("Initializing GenericRepositoryImpl");
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
            this.entityClass = (Class<T>) actualTypeArguments[1];
            log.debug("Entity class determined: {}", entityClass.getName());
        } else {
            log.error("Unable to determine the class type for GenericRepositoryImpl");
            throw new IllegalArgumentException("Unable to determine the class type");
        }
    }

    @Override
    @Transactional
    public void persist(T entity) {
        log.debug("Persisting entity: {}", entity);
        entityManager.persist(entity);
        log.debug("Entity persisted: {}", entity);
    }

    @Override
    @Transactional
    public void detach(T entity) {
        log.debug("Detaching entity: {}", entity);
        entityManager.detach(entity);
        log.debug("Entity detached: {}", entity);
    }

    @Override
    public T findById(I id) {
        log.debug("Finding entity by id: {}", id);
        T result = entityManager.find(entityClass, id);
        log.debug("Found entity: {}", result);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        log.debug("Finding all entities of type: {}", entityClass.getName());
        String query = "from " + entityClass.getName();
        Query managerQuery = entityManager.createQuery(query);
        List<T> resultList = managerQuery.getResultList();
        log.debug("Found {} entities", resultList.size());
        return resultList;
    }

    @Override
    public Long getCount() {
        log.debug("Getting count of entities of type: {}", entityClass.getName());
        String hql = "SELECT COUNT(a) FROM " + entityClass.getName() + " a";
        Query query = entityManager.createQuery(hql);
        Long count = (Long) query.getSingleResult();
        log.debug("Count: {}", count);
        return count;
    }

    @Override
    @Transactional
    public void removeById(I id) {
        log.debug("Removing entity by id: {}", id);
        try {
            T entity = entityManager.find(entityClass, id);
            entityManager.remove(entity);
            log.debug("Entity removed: {}", entity);
        } catch (IllegalArgumentException e) {
            log.error("Error removing entity by id: {}", id, e);
            throw new RepositoryException(entityClass.getName() + " with id:=" + id + " was not found");
        }
    }

    @Override
    @Transactional
    public void merge(T entity) {
        log.debug("Merging entity: {}", entity);
        entityManager.merge(entity);
        log.debug("Entity merged: {}", entity);
    }
}