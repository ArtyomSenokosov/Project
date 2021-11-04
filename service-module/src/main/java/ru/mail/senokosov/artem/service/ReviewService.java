package ru.mail.senokosov.artem.service;

import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.PageDTO;
import ru.mail.senokosov.artem.service.model.ReviewDTO;

public interface ReviewService {

    PageDTO getReviewsByPage(Integer page);

    boolean isDeleteById(Long id);

    void addReview(ReviewDTO reviewDTO) throws ServiceException;

    void changeReviewStatus(Long reviewId) throws ServiceException;
}