package ru.mail.senokosov.artem.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.ReviewRepository;
import ru.mail.senokosov.artem.repository.model.Review;

import javax.persistence.Query;
import java.util.List;

@Slf4j
@Repository
public class ReviewRepositoryImpl extends GenericRepositoryImpl<Long, Review> implements ReviewRepository {

    @Override
    @SuppressWarnings("unchecked")
    public List<Review> findAll(int startPosition, int maximumReviewsOnPage) {
        log.debug("Querying all reviews with startPosition: {}, maximumReviewsOnPage: {}",
                startPosition, maximumReviewsOnPage);
        String hql = "SELECT r FROM Review as r ORDER BY r.dateOfCreation ASC";
        Query query = entityManager.createQuery(hql);
        query.setFirstResult(startPosition);
        query.setMaxResults(maximumReviewsOnPage);
        List<Review> reviews = query.getResultList();
        log.debug("Found {} reviews", reviews.size());
        return reviews;
    }
}