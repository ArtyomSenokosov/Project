package ru.mail.senokosov.artem.service.converter;

import ru.mail.senokosov.artem.repository.model.Review;
import ru.mail.senokosov.artem.service.model.add.AddReviewDTO;
import ru.mail.senokosov.artem.service.model.show.ShowReviewDTO;

public interface ReviewConverter {

    ShowReviewDTO convert(Review review);

    Review convert(AddReviewDTO addReviewDTO);
}