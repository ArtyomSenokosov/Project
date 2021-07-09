package ru.mail.senokosov.artem.service.converter.impl;

import org.springframework.stereotype.Component;
import ru.mail.senokosov.artem.repository.model.Review;
import ru.mail.senokosov.artem.repository.model.Status;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.converter.ReviewConverter;
import ru.mail.senokosov.artem.service.model.add.AddReviewDTO;
import ru.mail.senokosov.artem.service.model.show.ShowReviewDTO;

import java.time.LocalDateTime;
import java.util.Objects;

import static ru.mail.senokosov.artem.service.util.ServiceUtil.getFormatDateTime;

@Component
public class ReviewConverterImpl implements ReviewConverter {

    @Override
    public ShowReviewDTO convert(Review review) {
        ShowReviewDTO showReviewDTO = new ShowReviewDTO();
        Long id = review.getId();
        showReviewDTO.setId(id);
        User user = review.getUser();
        if (Objects.nonNull(user)) {
            String lastName = user.getLastName();
            showReviewDTO.setLastName(lastName);
            String firstName = user.getFirstName();
            showReviewDTO.setFirstName(firstName);
            String middleName = user.getMiddleName();
            showReviewDTO.setMiddleName(middleName);
        }
        showReviewDTO.setReview(review.getReview());
        LocalDateTime date = review.getLocalDate();
        if (Objects.nonNull(date)) {
            String dateTime = getFormatDateTime(date);
            showReviewDTO.setLocalDateTime(dateTime);
        }
        if (Objects.nonNull(review.getStatus())) {
            Status status = review.getStatus();
            String statusName = status.getStatus();
            showReviewDTO.setStatus(statusName);
        }
        return showReviewDTO;
    }

    @Override
    public Review convert(AddReviewDTO addReviewDTO) {
        Review review = new Review();
        String addReview = addReviewDTO.getReview();
        if (Objects.nonNull(addReview)) {
            review.setReview(addReview);
        }
        return review;
    }
}