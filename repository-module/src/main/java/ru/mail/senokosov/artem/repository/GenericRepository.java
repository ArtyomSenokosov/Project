package ru.mail.senokosov.artem.repository;

import ru.mail.senokosov.artem.repository.model.ReviewStatus;

import java.util.List;

public interface GenericRepository<I, T> {

    void persist(T entity);

    void merge(T entity);

    void remove(T entity);

    T findById(I id);

    List<T> findAll();
}
