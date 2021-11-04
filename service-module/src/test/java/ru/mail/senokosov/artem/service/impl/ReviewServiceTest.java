package ru.mail.senokosov.artem.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.mail.senokosov.artem.repository.ReviewRepository;
import ru.mail.senokosov.artem.repository.ReviewStatusRepository;
import ru.mail.senokosov.artem.repository.UserRepository;
import ru.mail.senokosov.artem.repository.model.Review;
import ru.mail.senokosov.artem.repository.model.ReviewStatus;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.converter.ReviewConverter;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.PageDTO;
import ru.mail.senokosov.artem.service.model.ReviewDTO;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ReviewStatusRepository reviewStatusRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ReviewConverter reviewConverter;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Test
    void shouldReturnCorrectPageInfoWhenGettingReviewsByPage() {
        when(reviewRepository.getCount()).thenReturn(10L);
        when(reviewRepository.findAll(anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(new Review()));
        when(reviewConverter.convert(any(Review.class)))
                .thenReturn(new ReviewDTO());

        PageDTO result = reviewService.getReviewsByPage(1);

        assertNotNull(result);
        assertEquals(1, result.getReviews().size());

        verify(reviewRepository, times(1)).getCount();
        verify(reviewRepository, times(1)).findAll(anyInt(), anyInt());
    }

    @Test
    void shouldDeleteReviewByIdWhenReviewExists() {
        Long reviewId = 1L;

        when(reviewRepository.findById(reviewId)).thenReturn(new Review());

        boolean result = reviewService.isDeleteById(reviewId);

        assertTrue(result);
        verify(reviewRepository, times(1)).removeById(reviewId);
    }

    @Test
    void shouldReturnFalseWhenDeleteByIdThrowsException() {
        Long reviewId = 1L;

        when(reviewRepository.findById(anyLong())).thenThrow(new RuntimeException("Database error"));

        boolean result = reviewService.isDeleteById(reviewId);

        assertFalse(result);
        verify(reviewRepository, times(1)).findById(reviewId);
    }

    @Test
    void shouldAddReviewSuccessfullyWhenUserExists() {
        ReviewDTO reviewDTO = new ReviewDTO();
        User user = new User();
        user.setId(1L);
        String userEmail = "user@example.com";
        ReviewStatus reviewStatus = new ReviewStatus();
        reviewStatus.setStatusName("HIDE");

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(authentication.getName()).thenReturn(userEmail);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(userRepository.findUserByEmail(Mockito.eq(userEmail))).thenReturn(user);
        Mockito.when(reviewStatusRepository.findByStatusName(Mockito.eq("HIDE"))).thenReturn(reviewStatus);
        Mockito.when(reviewConverter.convert(Mockito.any(ReviewDTO.class))).thenReturn(new Review());

        assertDoesNotThrow(() -> reviewService.addReview(reviewDTO));

        Mockito.verify(reviewRepository, Mockito.times(1)).persist(Mockito.any(Review.class));

        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldThrowServiceExceptionWhenAuthenticatedUserNotFoundDuringAddReview() {
        ReviewDTO reviewDTO = new ReviewDTO();
        String userName = "user@example.com";

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn(userName);
        Mockito.when(userRepository.findUserByEmail(userName)).thenReturn(null);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        assertThrows(ServiceException.class, () -> reviewService.addReview(reviewDTO));
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldThrowServiceExceptionWhenNoAuthenticatedUserOnAddReview() {
        ReviewDTO reviewDTO = new ReviewDTO();

        SecurityContextHolder.clearContext();

        assertThrows(ServiceException.class, () -> reviewService.addReview(reviewDTO));
    }


    @Test
    void shouldChangeReviewStatusSuccessfullyWhenReviewFound() {
        Long reviewId = 1L;
        Review review = new Review();
        ReviewStatus initialStatus = new ReviewStatus();
        initialStatus.setStatusName("SHOW");
        review.setReviewStatus(initialStatus);

        when(reviewRepository.findById(reviewId)).thenReturn(review);
        when(reviewStatusRepository.findByStatusName("HIDE")).thenReturn(new ReviewStatus());

        assertDoesNotThrow(() -> reviewService.changeReviewStatus(reviewId));
        verify(reviewRepository, times(1)).merge(review);
    }

    @Test
    void shouldReturnFalseWhenReviewNotFoundOnDeleteById() {
        Long reviewId = 1L;
        when(reviewRepository.findById(reviewId)).thenReturn(null);

        boolean result = reviewService.isDeleteById(reviewId);

        assertFalse(result);
        verify(reviewRepository, times(1)).findById(reviewId);
    }

    @Test
    void shouldThrowServiceExceptionWhenUserNotFoundOnAddReview() {
        ReviewDTO reviewDTO = new ReviewDTO();

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        String userEmail = "nonexistent@example.com";

        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn(userEmail);

        Mockito.when(userRepository.findUserByEmail(userEmail)).thenReturn(null);

        assertThrows(ServiceException.class, () -> reviewService.addReview(reviewDTO));

        Mockito.verify(userRepository, Mockito.times(1)).findUserByEmail(userEmail);
    }

    @Test
    void shouldThrowServiceExceptionWhenReviewNotFoundOnChangeStatus() {
        Long reviewId = 1L;
        when(reviewRepository.findById(reviewId)).thenReturn(null);

        assertThrows(ServiceException.class, () -> reviewService.changeReviewStatus(reviewId));
        verify(reviewRepository, times(1)).findById(reviewId);
    }

    @Test
    void shouldChangeStatusFromShowToHideSuccessfullyWhenReviewFound() throws ServiceException {
        Long reviewId = 1L;
        Review review = new Review();
        ReviewStatus showStatus = new ReviewStatus();
        showStatus.setStatusName("SHOW");
        review.setReviewStatus(showStatus);

        ReviewStatus hideStatus = new ReviewStatus();
        hideStatus.setStatusName("HIDE");

        when(reviewRepository.findById(reviewId)).thenReturn(review);
        when(reviewStatusRepository.findByStatusName("HIDE")).thenReturn(hideStatus);

        reviewService.changeReviewStatus(reviewId);

        assertEquals("HIDE", review.getReviewStatus().getStatusName());
        verify(reviewRepository, times(1)).merge(review);
    }
}