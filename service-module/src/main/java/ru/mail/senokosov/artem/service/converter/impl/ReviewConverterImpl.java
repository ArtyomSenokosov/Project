package ru.mail.senokosov.artem.service.converter.impl;

import org.springframework.stereotype.Component;
import ru.mail.senokosov.artem.repository.model.Review;
import ru.mail.senokosov.artem.repository.model.Status;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.converter.ReviewConverter;
import ru.mail.senokosov.artem.service.model.ReviewDTO;

import java.time.LocalDate;

@Component
public class ReviewConverterImpl implements ReviewConverter {
    @Override
    public Review convert(ReviewDTO reviewDTO, Status status, User user) {
        Review review = new Review();
        String topic = reviewDTO.getTopic();
        review.setTopic(topic);
        String reviewName = reviewDTO.getReview();
        review.setReview(reviewName);
        LocalDate localDate = reviewDTO.getLocalDate();
        review.setLocalDate(localDate);
        review.setStatus(status);
        review.setUser(user);
        return review;
    }

    @Override
    public ReviewDTO convert(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        Long id = review.getId();
        reviewDTO.setId(id);
        String topic = review.getTopic();
        reviewDTO.setTopic(topic);
        String reviewName = review.getReview();
        reviewDTO.setReview(reviewName);
        LocalDate localDate = review.getLocalDate();
        reviewDTO.setLocalDate(localDate);
        Status status = review.getStatus();
        reviewDTO.setStatus(status);
        User user = review.getUser();
        reviewDTO.setUser(user);
        return reviewDTO;
    }
}
