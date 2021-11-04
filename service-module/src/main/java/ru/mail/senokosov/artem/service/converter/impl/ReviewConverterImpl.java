package ru.mail.senokosov.artem.service.converter.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.mail.senokosov.artem.repository.model.Review;
import ru.mail.senokosov.artem.repository.model.ReviewStatus;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.converter.ReviewConverter;
import ru.mail.senokosov.artem.service.model.ReviewDTO;

import java.time.LocalDateTime;
import java.util.Objects;

import static ru.mail.senokosov.artem.service.util.ServiceUtil.getFormatDateTime;

@Component
@RequiredArgsConstructor
public class ReviewConverterImpl implements ReviewConverter {

    private final ModelMapper modelMapper;

    @Override
    public ReviewDTO convert(Review review) {
        ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);

        User user = review.getUser();
        if (Objects.nonNull(user)) {
            String lastName = user.getLastName();
            reviewDTO.setLastName(lastName);
            String firstName = user.getFirstName();
            reviewDTO.setFirstName(firstName);
        }

        LocalDateTime date = review.getDateOfCreation();
        if (Objects.nonNull(date)) {
            String dateTime = getFormatDateTime(date);
            reviewDTO.setDateOfCreation(dateTime);
        }

        if (Objects.nonNull(review.getReviewStatus())) {
            ReviewStatus reviewStatus = review.getReviewStatus();
            String statusName = reviewStatus.getStatusName();
            reviewDTO.setStatus(statusName);
        }

        return reviewDTO;
    }

    @Override
    public Review convert(ReviewDTO reviewDTO) {
        Review review = new Review();

        String addReview = reviewDTO.getContent();
        if (Objects.nonNull(addReview)) {
            review.setContent(addReview);
        }

        return review;
    }
}