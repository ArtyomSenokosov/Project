package ru.mail.senokosov.artem.service;

import org.hibernate.service.spi.ServiceException;
import org.springframework.data.domain.Page;
import ru.mail.senokosov.artem.service.model.ReviewDTO;
import ru.mail.senokosov.artem.service.model.add.AddReviewDTO;
import ru.mail.senokosov.artem.service.model.show.ShowReviewDTO;

import java.util.List;

public interface ReviewService {

    Page<ShowReviewDTO> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

    List<ShowReviewDTO> getAllReviews();

    void persist(AddReviewDTO addReviewDTO);

    boolean deleteReviewById(Long id) throws SecurityException;

    ReviewDTO changeStatusById(ReviewDTO reviewDTO) throws ServiceException;
}
