package ru.mail.senokosov.artem.repository;

import ru.mail.senokosov.artem.repository.model.Review;

import java.util.List;

public interface ReviewRepository extends GenericRepository<Long, Review> {

    List<Review> findAll(int startPosition, int maximumReviewsOnPage);
}