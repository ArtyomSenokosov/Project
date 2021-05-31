package ru.mail.senokosov.artem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.service.spi.ServiceException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import ru.mail.senokosov.artem.repository.PagingAndSortingRepository;
import ru.mail.senokosov.artem.repository.ReviewRepository;
import ru.mail.senokosov.artem.repository.UserRepository;
import ru.mail.senokosov.artem.repository.model.*;
import ru.mail.senokosov.artem.service.ReviewService;
import ru.mail.senokosov.artem.service.converter.ReviewConverter;
import ru.mail.senokosov.artem.service.model.ReviewDTO;
import ru.mail.senokosov.artem.service.model.add.AddReviewDTO;
import ru.mail.senokosov.artem.service.model.show.ShowReviewDTO;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewConverter reviewConverter;
    private final UserRepository userRepository;
    private final ThreadLocal<PagingAndSortingRepository> pagingAndSortingRepository = new ThreadLocal<PagingAndSortingRepository>();

    @Override
    @Transactional
    public Page<ShowReviewDTO> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.pagingAndSortingRepository.get().findAll(pageable);
    }

    @Override
    @Transactional
    public List<ShowReviewDTO> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream()
                .map(reviewConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void persist(AddReviewDTO addReviewDTO) {
        Long userId = addReviewDTO.getUserId();
        Review review = reviewConverter.convert(addReviewDTO);
        User user = userRepository.findById(userId);
        if (Objects.nonNull(user)) {
            review.setUser(user);
        }
        reviewRepository.persist(review);
    }

    @Override
    @Transactional
    public boolean deleteReviewById(Long id) throws SecurityException {
        Review byId = reviewRepository.findById(id);
        reviewRepository.remove(byId);
        return true;
    }

    @Override
    @Transactional
    public ReviewDTO changeStatusById(ReviewDTO reviewDTO) throws ServiceException {
        Review review = reviewRepository.findById(reviewDTO.getId());
        ReviewStatus status = reviewDTO.getStatus();
        review.setReviewStatus(status);
        reviewRepository.merge(review);
        return reviewConverter.convertToChange(review);
    }
}
