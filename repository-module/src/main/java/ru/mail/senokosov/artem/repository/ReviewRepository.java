package ru.mail.senokosov.artem.repository;

import ru.mail.senokosov.artem.repository.model.Review;

import java.util.List;

public interface ReviewRepository extends GenericRepository<Long, Review> {

    Long getCountReviews();

    List<Review> findAll(int startPosition, int maximumReviewsOnPage);

    List<Review> findShowReviews(Integer startPosition, int maximumReviewsOnPage, String statusName);
}