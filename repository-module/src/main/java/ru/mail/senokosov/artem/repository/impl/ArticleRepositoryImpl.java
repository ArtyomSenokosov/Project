package ru.mail.senokosov.artem.repository.impl;

import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.ArticleRepository;
import ru.mail.senokosov.artem.repository.model.Article;
import ru.mail.senokosov.artem.repository.model.User;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import static ru.mail.senokosov.artem.repository.constant.RepositoryConstants.DATE_PARAMETER;

@Repository
public class ArticleRepositoryImpl extends GenericRepositoryImpl<Long, Article> implements ArticleRepository {

    @Override
    public Long getCountArticle() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(User.class)));
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Article> findAll(int pageNumber, int pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(Article.class)));
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        CriteriaQuery<Article> articleQuery = criteriaBuilder.createQuery(Article.class);
        Root<Article> articleRoot = articleQuery.from(Article.class);
        CriteriaQuery<Article> select = articleQuery.select(articleRoot)
                .orderBy(criteriaBuilder.asc(articleRoot.get(DATE_PARAMETER)));
        TypedQuery<Article> typedQuery = entityManager.createQuery(select);
        if (pageSize < count.intValue()) {
            typedQuery.setFirstResult((pageNumber) = pageSize - pageSize);
            typedQuery.setMaxResults(pageSize);
            return typedQuery.getResultList();
        }
        return typedQuery.getResultList();
    }
}
