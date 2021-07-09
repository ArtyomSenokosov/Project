package ru.mail.senokosov.artem.repository;

import ru.mail.senokosov.artem.repository.model.Article;

import java.util.List;

public interface ArticleRepository extends GenericRepository<Long, Article> {

    Long getCountArticles();

    List<Article> findAll(Integer startPosition, Integer maximumArticlesOnPage);
}