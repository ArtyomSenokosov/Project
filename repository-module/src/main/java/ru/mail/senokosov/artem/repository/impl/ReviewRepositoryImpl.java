package ru.mail.senokosov.artem.repository.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.ReviewRepository;
import ru.mail.senokosov.artem.repository.model.Review;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;

import static ru.mail.senokosov.artem.repository.constant.RepositoryConstants.DATE_PARAMETER;
import static ru.mail.senokosov.artem.repository.constant.RepositoryConstants.ID_PARAMETER;

@Repository
@Log4j2
public class ReviewRepositoryImpl extends GenericRepositoryImpl<Long, Review> implements ReviewRepository {

    @Override
    public Long getCountReviews() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(Review.class)));
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Review> findAll(int startPosition, int maximumReviewsOnPage) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Review> reviewQuery = criteriaBuilder.createQuery(Review.class);
        Root<Review> reviewRoot = reviewQuery.from(Review.class);
        CriteriaQuery<Review> select = reviewQuery.select(reviewRoot)
                .orderBy(criteriaBuilder.asc(reviewRoot.get(DATE_PARAMETER)));
        TypedQuery<Review> typedQuery = entityManager.createQuery(select);
        typedQuery.setFirstResult(startPosition);
        typedQuery.setMaxResults(maximumReviewsOnPage);
        return typedQuery.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Review> findShowReviews(Integer startPosition, int maximumReviewsOnPage, String statusName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Review> reviewQuery = criteriaBuilder.createQuery(Review.class);
        Root<Review> reviewRoot = reviewQuery.from(Review.class);
        CriteriaQuery<Review> select = reviewQuery.select(reviewRoot)
                .orderBy(criteriaBuilder.asc(reviewRoot.get(DATE_PARAMETER)));
        TypedQuery<Review> typedQuery = entityManager.createQuery(select);
        typedQuery.setFirstResult(startPosition);
        typedQuery.setMaxResults(maximumReviewsOnPage);
        try {
            ParameterExpression<Long> parameter = criteriaBuilder.parameter(Long.class);
            reviewQuery.where(criteriaBuilder.equal(reviewRoot.get(ID_PARAMETER), parameter));
            typedQuery.setParameter(String.valueOf(parameter), statusName);
            return typedQuery.getResultList();
        } catch (NoResultException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}