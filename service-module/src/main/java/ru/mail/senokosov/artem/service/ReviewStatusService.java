package ru.mail.senokosov.artem.service;

import ru.mail.senokosov.artem.service.model.show.ShowReviewStatusDTO;

import java.util.List;

public interface ReviewStatusService {

    List<ShowReviewStatusDTO> getAll();
}
