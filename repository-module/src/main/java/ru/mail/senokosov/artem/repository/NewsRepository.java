package ru.mail.senokosov.artem.repository;

import ru.mail.senokosov.artem.repository.model.News;

import java.util.List;

public interface NewsRepository extends GenericRepository<Long, News> {

    List<News> findAll(Integer startPosition, Integer maximumNewsOnPage);
}