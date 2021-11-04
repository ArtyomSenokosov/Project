package ru.mail.senokosov.artem.service.converter;

import ru.mail.senokosov.artem.repository.model.Review;
import ru.mail.senokosov.artem.service.model.ReviewDTO;

public interface ReviewConverter {

    ReviewDTO convert(Review review);

    Review convert(ReviewDTO reviewDTO);
}