package ru.mail.senokosov.artem.repository.impl;

import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.ReviewRepository;
import ru.mail.senokosov.artem.repository.model.Review;

import javax.persistence.Query;
import java.util.List;

@Repository
public class ReviewRepositoryImpl extends GenericRepositoryImpl<Long, Review> implements ReviewRepository {

    @Override
    public Long getCountReviews() {
        String hql = "SELECT COUNT(r.id) FROM Review as r";
        Query query = entityManager.createQuery(hql);
        return (Long) query.getSingleResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Review> findAll(int startPosition, int maximumReviewsOnPage) {
        String hql = "SELECT r FROM Review as r ORDER BY r.localDate ASC";
        Query query = entityManager.createQuery(hql);
        query.setFirstResult(startPosition);
        query.setMaxResults(maximumReviewsOnPage);
        return query.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Review> findShowReviews(Integer startPosition, int maximumReviewsOnPage, String statusName) {
        String hql = "SELECT r FROM Review as r WHERE r.status.status=:statusName ORDER BY r.localDate ASC";
        Query query = entityManager.createQuery(hql);
        query.setParameter("statusName", statusName);
        query.setFirstResult(startPosition);
        query.setMaxResults(maximumReviewsOnPage);
        return query.getResultList();
    }
}