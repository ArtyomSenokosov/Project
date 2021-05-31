package ru.mail.senokosov.artem.service.converter.impl;

import org.springframework.stereotype.Component;
import ru.mail.senokosov.artem.repository.model.ReviewStatus;
import ru.mail.senokosov.artem.service.converter.ReviewStatusConverter;
import ru.mail.senokosov.artem.service.model.show.ShowReviewStatusDTO;

@Component
public class ReviewStatusConverterImpl implements ReviewStatusConverter {
    @Override
    public ShowReviewStatusDTO convert(ReviewStatus reviewStatus) {
        ShowReviewStatusDTO showReviewStatusDTO = new ShowReviewStatusDTO();
        Long id = reviewStatus.getId();
        showReviewStatusDTO.setId(id);
        String status = String.valueOf(reviewStatus.getStatus());
        showReviewStatusDTO.setStatus(status);
        return showReviewStatusDTO;
    }
}
