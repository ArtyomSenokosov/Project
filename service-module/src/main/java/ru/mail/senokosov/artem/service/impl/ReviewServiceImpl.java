package ru.mail.senokosov.artem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.service.spi.ServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.mail.senokosov.artem.repository.ReviewRepository;
import ru.mail.senokosov.artem.repository.StatusRepository;
import ru.mail.senokosov.artem.repository.UserRepository;
import ru.mail.senokosov.artem.repository.model.*;
import ru.mail.senokosov.artem.service.ReviewService;
import ru.mail.senokosov.artem.service.converter.ReviewConverter;
import ru.mail.senokosov.artem.service.model.PageDTO;
import ru.mail.senokosov.artem.service.model.enums.StatusDTOEnum;
import ru.mail.senokosov.artem.service.model.add.AddReviewDTO;
import ru.mail.senokosov.artem.service.model.show.ShowReviewDTO;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.mail.senokosov.artem.service.constant.ReviewPaginateConstant.MAXIMUM_REVIEWS_ON_PAGE;
import static ru.mail.senokosov.artem.service.util.SecurityUtil.getAuthentication;
import static ru.mail.senokosov.artem.service.util.ServiceUtil.getPageDTO;

@RequiredArgsConstructor
@Log4j2
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewConverter reviewConverter;
    private final StatusRepository statusRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void deleteById(Long id) {
        reviewRepository.removeById(id);
    }

    @Override
    @Transactional
    public void changeStatusByIds(List<Long> checkedIds, List<Long> allIds) {
        List<Status> statuses = statusRepository.findAll();
        if (checkedIds == null) {
            List<Review> reviews = reviewRepository.findAll();
            for (Review review : reviews) {
                Status statusHide = statuses.get(1);
                statusHide.getReviews().add(review);
            }
        } else {
            log.info("allIds: {}", allIds);
            log.info("checkedIds: {}", checkedIds);
            List<Long> uncheckedIds = removeCheckedId(checkedIds, allIds);
            log.info("uncheckedId: {}", uncheckedIds);
            for (Long id : uncheckedIds) {
                Review uncheckedReview = reviewRepository.findById(id);
                changeStatusToHide(statuses, uncheckedReview);
            }
            for (Long id : checkedIds) {
                Review checkedReview = reviewRepository.findById(id);
                changeStatusToShow(statuses, checkedReview);
            }
        }
    }

    @Override
    @Transactional
    public PageDTO getReviewsByPage(Integer page) {
        Long countReviews = reviewRepository.getCountReviews();
        PageDTO pageDTO = getPageDTO(page, countReviews, MAXIMUM_REVIEWS_ON_PAGE);
        log.info("pageDTO: {}", pageDTO);
        List<Review> reviews = reviewRepository.findAll(pageDTO.getStartPosition(), MAXIMUM_REVIEWS_ON_PAGE);
        pageDTO.getReviews().addAll(reviews.stream()
                .map(reviewConverter::convert)
                .collect(Collectors.toList()));
        return pageDTO;
    }

    @Override
    @Transactional
    public ShowReviewDTO add(AddReviewDTO addReviewDTO) throws ServiceException {
        Authentication authentication = getAuthentication();
        String name = authentication.getName();
        User user = userRepository.findUserByUsername(name);
        if (Objects.nonNull(user)) {
            Review review = getReview(addReviewDTO, user);
            reviewRepository.persist(review);
            return reviewConverter.convert(review);
        } else {
            throw new ServiceException(String.format("User with username: %s was not found", name));
        }
    }

    @Override
    @Transactional
    public PageDTO getShowReviewsByPage(int page) {
        Long countReviews = reviewRepository.getCountReviews();
        PageDTO pageDTO = getPageDTO(page, countReviews, MAXIMUM_REVIEWS_ON_PAGE);
        log.info("pageDTO: {}", pageDTO);
        List<Review> reviews = reviewRepository.findShowReviews(pageDTO.getStartPosition(), MAXIMUM_REVIEWS_ON_PAGE, StatusDTOEnum.SHOW.name());
        pageDTO.getReviews().addAll(reviews.stream()
                .map(reviewConverter::convert)
                .collect(Collectors.toList()));
        return pageDTO;
    }

    private Review getReview(AddReviewDTO addReviewDTO, User user) throws ServiceException {
        String statusName = StatusDTOEnum.HIDE.name();
        Status status = statusRepository.findByStatusName(statusName);
        if (Objects.nonNull(status)) {
            Review review = reviewConverter.convert(addReviewDTO);
            review.setStatus(status);
            review.setUser(user);
            LocalDateTime localDateTime = LocalDateTime.now();
            review.setLocalDate(localDateTime);
            return review;
        } else {
            throw new ServiceException(String.format("Status with status name: %s was not found", statusName));
        }
    }


    private List<Long> removeCheckedId(List<Long> checkedIds, List<Long> allIds) {
        for (Long checkedId : checkedIds) {
            for (int i = 0; i < allIds.size(); i++) {
                if (allIds.get(i).equals(checkedId)) {
                    allIds.remove(allIds.get(i));
                }
            }
        }
        return allIds;
    }

    private void changeStatusToHide(List<Status> statuses, Review uncheckedReview) {
        if (uncheckedReview.getStatus().equals(statuses.get(0))) {
            Status statusHide = statuses.get(1);
            statusHide.getReviews().add(uncheckedReview);
        }
    }

    private void changeStatusToShow(List<Status> statuses, Review checkedReview) {
        if (checkedReview.getStatus().equals(statuses.get(1))) {
            Status statusShow = statuses.get(0);
            statusShow.getReviews().add(checkedReview);
        }
    }
}