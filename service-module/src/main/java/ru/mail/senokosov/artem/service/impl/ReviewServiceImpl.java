package ru.mail.senokosov.artem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.mail.senokosov.artem.repository.ReviewRepository;
import ru.mail.senokosov.artem.repository.ReviewStatusRepository;
import ru.mail.senokosov.artem.repository.UserRepository;
import ru.mail.senokosov.artem.repository.model.Review;
import ru.mail.senokosov.artem.repository.model.ReviewStatus;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.ReviewService;
import ru.mail.senokosov.artem.service.converter.ReviewConverter;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.PageDTO;
import ru.mail.senokosov.artem.service.model.PaginationResult;
import ru.mail.senokosov.artem.service.model.ReviewDTO;
import ru.mail.senokosov.artem.service.model.enums.ReviewStatusDTOEnum;
import ru.mail.senokosov.artem.service.util.PaginationUtil;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.mail.senokosov.artem.service.constant.ReviewConstant.MAXIMUM_REVIEWS_ON_PAGE;
import static ru.mail.senokosov.artem.service.util.SecurityUtil.getAuthentication;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewStatusRepository reviewStatusRepository;
    private final UserRepository userRepository;
    private final ReviewConverter reviewConverter;

    @Override
    @Transactional
    public PageDTO getReviewsByPage(Integer page) {
        log.debug("Requesting page number {} for reviews.", page);

        Long countReviews = reviewRepository.getCount();

        PaginationResult pagination = PaginationUtil.calculatePagination(countReviews, MAXIMUM_REVIEWS_ON_PAGE, page);

        List<Review> reviews = reviewRepository.findAll(pagination.getStartPosition(), MAXIMUM_REVIEWS_ON_PAGE);
        log.debug("Retrieved {} reviews for page {}.", reviews.size(), pagination.getCurrentPage());

        List<ReviewDTO> reviewDTOs = reviews.stream()
                .map(reviewConverter::convert)
                .collect(Collectors.toList());

        PageDTO pageDTO = new PageDTO();
        pageDTO.setTotalPages(pagination.getTotalPages());
        pageDTO.setCurrentPage((long) pagination.getCurrentPage());
        pageDTO.setReviews(reviewDTOs);
        pageDTO.setStartPosition(pagination.getStartPosition());

        log.info("Page {} of reviews served with {} reviews.", pagination.getCurrentPage(), reviewDTOs.size());
        return pageDTO;
    }

    @Override
    @Transactional
    public boolean isDeleteById(Long id) {
        try {
            if (Objects.nonNull(reviewRepository.findById(id))) {
                reviewRepository.removeById(id);
                log.info("Review with id {} has been successfully deleted.", id);
                return true;
            } else {
                log.warn("Review with id {} not found. Deletion cannot be performed.", id);
                return false;
            }
        } catch (Exception exception) {
            log.error("Error occurred during the deletion of review with id {}: {}", id, exception.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public void addReview(ReviewDTO reviewDTO) throws ServiceException {
        log.debug("Starting process to add a new review by user.");

        Authentication authentication = getAuthentication();
        if (Objects.nonNull(authentication)) {
            String userName = authentication.getName();
            User user = userRepository.findUserByEmail(userName);

            if (Objects.nonNull(user)) {
                Review review = getReview(reviewDTO, user);
                reviewRepository.persist(review);
                log.info("Review has been successfully added by user: {}", userName);
            } else {
                log.error("User with username: {} was not found, unable to add review.", userName);
                throw new ServiceException(String.format("User with username: %s was not found", userName));
            }
        } else {
            log.error("An attempt was made to add a review without authentication.");
            throw new ServiceException("A user without authentication tries to add a review.");
        }
    }

    @Override
    @Transactional
    public void changeReviewStatus(Long reviewId) throws ServiceException {
        log.debug("Attempting to change status for review with id: {}", reviewId);

        Review review = reviewRepository.findById(reviewId);
        if (Objects.isNull(review)) {
            log.warn("Review with id: {} was not found", reviewId);
            throw new ServiceException(String.format("Review with id: %s was not found", reviewId));
        }

        ReviewStatus currentReviewStatus = review.getReviewStatus();
        ReviewStatus newReviewStatus = reverseReviewStatus(currentReviewStatus);

        review.setReviewStatus(newReviewStatus);
        reviewRepository.merge(review);

        log.info("Successfully changed the status of review with id: {} to '{}'",
                reviewId, newReviewStatus.getStatusName());
    }

    private ReviewStatus reverseReviewStatus(ReviewStatus currentStatus) {
        if (currentStatus.getStatusName().equals(ReviewStatusDTOEnum.SHOW.name())) {
            return reviewStatusRepository.findByStatusName(ReviewStatusDTOEnum.HIDE.name());
        } else {
            return reviewStatusRepository.findByStatusName(ReviewStatusDTOEnum.SHOW.name());
        }
    }

    private Review getReview(ReviewDTO addReviewDTO, User user) throws ServiceException {
        String statusName = ReviewStatusDTOEnum.HIDE.name();
        ReviewStatus reviewStatus = reviewStatusRepository.findByStatusName(statusName);
        if (Objects.nonNull(reviewStatus)) {
            Review review = reviewConverter.convert(addReviewDTO);
            review.setReviewStatus(reviewStatus);
            review.setUser(user);
            LocalDateTime localDateTime = LocalDateTime.now();
            review.setDateOfCreation(localDateTime);
            return review;
        } else {
            throw new ServiceException(String.format("Status with status name: %s was not found", statusName));
        }
    }
}