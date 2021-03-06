package ru.mail.senokosov.artem.repository.impl;

import lombok.extern.log4j.Log4j2;
import ru.mail.senokosov.artem.repository.GenericRepository;
import ru.mail.senokosov.artem.repository.exception.RepositoryException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.ParameterizedType;
import java.util.List;

@Log4j2
public class GenericRepositoryImpl<I, T> implements GenericRepository<I, T> {

    @PersistenceContext
    protected EntityManager entityManager;

    protected Class<T> entityClass;

    public GenericRepositoryImpl() {
        ParameterizedType genericClass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericClass.getActualTypeArguments()[1];
    }

    @Override
    public void persist(T entity) {
        entityManager.persist(entity);
    }

    @Override
    public void detach(T entity) {
        entityManager.detach(entity);
    }

    @Override
    public T findById(I id) {
        return entityManager.find(entityClass, id);
    }

    @Override
    public void removeById(I id) {
        try {
            T entity = entityManager.find(entityClass, id);
            entityManager.remove(entity);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            throw new RepositoryException(entityClass.getName() + " with id:=" + id + " was not found");
        }

    }

    @Override
    public void merge(T entity) {
        entityManager.merge(entity);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        String query = "from " + entityClass.getName();
        Query managerQuery = entityManager.createQuery(query);
        return managerQuery.getResultList();
    }
}