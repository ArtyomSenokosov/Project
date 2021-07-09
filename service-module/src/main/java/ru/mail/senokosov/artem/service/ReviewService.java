package ru.mail.senokosov.artem.service;

import org.hibernate.service.spi.ServiceException;
import ru.mail.senokosov.artem.service.model.PageDTO;
import ru.mail.senokosov.artem.service.model.add.AddReviewDTO;
import ru.mail.senokosov.artem.service.model.show.ShowReviewDTO;

import java.util.List;

public interface ReviewService {

    void deleteById(Long id);

    void changeStatusByIds(List<Long> checkedIds, List<Long> allIds);

    PageDTO getReviewsByPage(Integer page);

    ShowReviewDTO add(AddReviewDTO addReviewDTO) throws ServiceException;

    PageDTO getShowReviewsByPage(int page);
}