package ru.mail.senokosov.artem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.mail.senokosov.artem.repository.ReviewStatusRepository;
import ru.mail.senokosov.artem.repository.model.ReviewStatus;
import ru.mail.senokosov.artem.service.ReviewStatusService;
import ru.mail.senokosov.artem.service.converter.ReviewStatusConverter;
import ru.mail.senokosov.artem.service.model.show.ShowReviewStatusDTO;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewStatusServiceImpl implements ReviewStatusService {

    private final ReviewStatusRepository reviewStatusRepository;
    private final ReviewStatusConverter reviewStatusConverter;

    @Override
    @Transactional
    public List<ShowReviewStatusDTO> getAll() {
        List<ReviewStatus> reviewStatuses = reviewStatusRepository.findAll();
        return reviewStatuses.stream()
                .map(reviewStatusConverter::convert)
                .collect(Collectors.toList());
    }
}
