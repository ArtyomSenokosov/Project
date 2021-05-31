package ru.mail.senokosov.artem.service.converter;

import ru.mail.senokosov.artem.repository.model.Article;
import ru.mail.senokosov.artem.repository.model.Review;
import ru.mail.senokosov.artem.repository.model.ReviewStatus;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.model.ArticleDTO;
import ru.mail.senokosov.artem.service.model.ReviewDTO;
import ru.mail.senokosov.artem.service.model.add.AddArticleDTO;
import ru.mail.senokosov.artem.service.model.add.AddReviewDTO;
import ru.mail.senokosov.artem.service.model.show.ShowArticleDTO;
import ru.mail.senokosov.artem.service.model.show.ShowReviewDTO;

import java.util.List;

public interface ReviewConverter {

    ShowReviewDTO convert(Review review);

    Review convert(AddReviewDTO addReviewDTO);

    ReviewDTO convertToChange(Review review);
}
