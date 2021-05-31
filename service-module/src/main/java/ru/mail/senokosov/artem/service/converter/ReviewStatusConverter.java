package ru.mail.senokosov.artem.service.converter;

import ru.mail.senokosov.artem.repository.model.OrderStatus;
import ru.mail.senokosov.artem.repository.model.ReviewStatus;
import ru.mail.senokosov.artem.service.model.show.ShowOrderStatusDTO;
import ru.mail.senokosov.artem.service.model.show.ShowReviewStatusDTO;

public interface ReviewStatusConverter {

    ShowReviewStatusDTO convert(ReviewStatus reviewStatus);
}
