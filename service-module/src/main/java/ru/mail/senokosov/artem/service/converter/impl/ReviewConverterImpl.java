package ru.mail.senokosov.artem.service.converter.impl;

import org.springframework.stereotype.Component;
import ru.mail.senokosov.artem.repository.model.Review;
import ru.mail.senokosov.artem.repository.model.ReviewStatus;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.converter.ReviewConverter;
import ru.mail.senokosov.artem.service.model.ReviewDTO;
import ru.mail.senokosov.artem.service.model.add.AddReviewDTO;
import ru.mail.senokosov.artem.service.model.show.ShowReviewDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
            String firstname = user.getFirstname();
            showReviewDTO.setFirstname(firstname);
            String secondname = user.getSecondname();
            showReviewDTO.setSecondname(secondname);
            String middlename = user.getMiddlename();
            showReviewDTO.setMiddlename(middlename);
        }
        LocalDateTime localDateTime = review.getLocalDate();
        if (Objects.nonNull(localDateTime)) {
            String formatDateTime = getFormatDateTime(localDateTime);
            showReviewDTO.setDate(formatDateTime);
        }
        String topic = review.getTopic();
        showReviewDTO.setTopic(topic);
        String content = review.getReview();
        showReviewDTO.setReview(content);
        return showReviewDTO;
    }

    @Override
    public Review convert(AddReviewDTO addReviewDTO) {
        Review review = new Review();
        String topic = addReviewDTO.getTopic();
        review.setTopic(topic);
        String content = addReviewDTO.getReview();
        review.setReview(content);
        return review;
    }

    @Override
    public ReviewDTO convertToChange(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        Long id = review.getId();
        reviewDTO.setId(id);
        ReviewStatus status = review.getReviewStatus();
        reviewDTO.setStatus(status);
        return reviewDTO;
    }
}
