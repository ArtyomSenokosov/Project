package ru.mail.senokosov.artem.repository.impl;

import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.ArticleRepository;
import ru.mail.senokosov.artem.repository.model.Article;

import javax.persistence.Query;
import java.util.List;

@Repository
public class ArticleRepositoryImpl extends GenericRepositoryImpl<Long, Article> implements ArticleRepository {

    @Override
    public Long getCountArticles() {
        String hql = "SELECT COUNT(a.id) FROM Article as a";
        Query query = entityManager.createQuery(hql);
        return (Long) query.getSingleResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Article> findAll(Integer startPosition, Integer maximumArticlesOnPage) {
        String hql = "SELECT a FROM Article as a ORDER BY a.localDateTime DESC";
        Query query = entityManager.createQuery(hql);
        query.setFirstResult(startPosition);
        query.setMaxResults(maximumArticlesOnPage);
        return query.getResultList();
    }
}