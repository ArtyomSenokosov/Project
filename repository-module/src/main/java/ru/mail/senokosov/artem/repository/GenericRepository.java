package ru.mail.senokosov.artem.repository;

import java.util.List;

public interface GenericRepository<I, T> {

    void persist(T entity);

    void detach(T entity);

    T findById(I id);

    List<T> findAll();

    void removeById(I id);

    void merge(T entity);
}