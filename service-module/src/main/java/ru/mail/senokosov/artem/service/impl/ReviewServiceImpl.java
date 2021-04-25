package ru.mail.senokosov.artem.service.impl;

import org.springframework.stereotype.Service;
import ru.mail.senokosov.artem.repository.ReviewRepository;
import ru.mail.senokosov.artem.repository.model.Review;
import ru.mail.senokosov.artem.repository.model.Status;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.ReviewService;
import ru.mail.senokosov.artem.service.converter.ReviewConverter;
import ru.mail.senokosov.artem.service.model.ReviewDTO;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewConverter reviewConverter;

    public ReviewServiceImpl(ReviewRepository reviewRepository, ReviewConverter reviewConverter) {
        this.reviewRepository = reviewRepository;
        this.reviewConverter = reviewConverter;
    }

    @Override
    @Transactional
    public List<ReviewDTO> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        List<ReviewDTO> reviewsDTO = new ArrayList<>();
        for (Review review : reviews) {
            reviewsDTO.add(reviewConverter.convert(review));
        }
        return reviewsDTO;
    }

    @Override
    @Transactional
    public void addReview(ReviewDTO reviewDTO, Status status, User user) {
        Review review = reviewConverter.convert(reviewDTO, status, user);
        reviewRepository.persist(review);
    }

    @Override
    @Transactional
    public Optional<ReviewDTO> deleteReview(Long id) {
        Review review = reviewRepository.findById(id);
        reviewRepository.remove(review);
        return Optional.empty();
    }
}
