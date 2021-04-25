package ru.mail.senokosov.artem.service.converter;

import ru.mail.senokosov.artem.repository.model.Review;
import ru.mail.senokosov.artem.repository.model.Status;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.model.ReviewDTO;

public interface ReviewConverter {

    Review convert(ReviewDTO reviewDTO, Status status, User user);

    ReviewDTO convert(Review review);
}
