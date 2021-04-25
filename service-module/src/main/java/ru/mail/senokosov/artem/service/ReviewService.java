package ru.mail.senokosov.artem.service;

import ru.mail.senokosov.artem.repository.model.Status;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.model.ReviewDTO;

import java.util.List;
import java.util.Optional;

public interface ReviewService {

    List<ReviewDTO> getAllReviews();

    void addReview(ReviewDTO reviewDTO, Status status, User user);

    Optional<ReviewDTO> deleteReview(Long id);
}
