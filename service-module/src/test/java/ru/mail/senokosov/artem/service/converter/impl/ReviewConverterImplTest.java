package ru.mail.senokosov.artem.service.converter.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import ru.mail.senokosov.artem.repository.model.Review;
import ru.mail.senokosov.artem.repository.model.ReviewStatus;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.model.ReviewDTO;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ReviewConverterImplTest {

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ReviewConverterImpl reviewConverter;

    private Review review;
    private ReviewDTO reviewDTO;

    @BeforeEach
    void setUp() {
        LocalDateTime reviewDate = LocalDateTime.of(2024, 3, 20, 12, 0);

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");

        ReviewStatus reviewStatus = new ReviewStatus();
        reviewStatus.setStatusName("Approved");

        review = new Review();
        review.setDateOfCreation(reviewDate);
        review.setContent("This is a sample review content.");
        review.setUser(user);
        review.setReviewStatus(reviewStatus);

        reviewDTO = new ReviewDTO();
        reviewDTO.setDateOfCreation("2024-03-20T12:00");
        reviewDTO.setFirstName(user.getFirstName());
        reviewDTO.setLastName(user.getLastName());
        reviewDTO.setStatus(reviewStatus.getStatusName());

        when(modelMapper.map(any(Review.class), any())).thenReturn(reviewDTO);
    }

    @Test
    void shouldCorrectlyConvertToDTOWithAllFieldsSet() {
        when(modelMapper.map(any(Review.class), any())).thenReturn(reviewDTO);

        reviewConverter.convert(review);
    }

    @Test
    void shouldHandleUserFieldsWhenUserIsPresent() {
        when(modelMapper.map(any(Review.class), any())).thenReturn(reviewDTO);

        ReviewDTO resultDTO = reviewConverter.convert(review);

        assertEquals("John", resultDTO.getFirstName());
        assertEquals("Doe", resultDTO.getLastName());
    }

    @Test
    void shouldHandleDateOfCreationWhenNotNull() {
        when(modelMapper.map(any(Review.class), any())).thenReturn(reviewDTO);
        String expectedFormattedDate = "2024-03-20 12:00:00";

        ReviewDTO resultDTO = reviewConverter.convert(review);

        assertEquals(expectedFormattedDate, resultDTO.getDateOfCreation(), "The date of creation should match the expected formatted date.");
    }

    @Test
    void shouldSetReviewStatusCorrectly() {
        when(modelMapper.map(any(Review.class), any())).thenReturn(reviewDTO);

        ReviewDTO resultDTO = reviewConverter.convert(review);

        assertEquals("Approved", resultDTO.getStatus());
    }

    @Test
    void shouldNotAlterUserRelatedFieldsWhenUserInfoIsAbsent() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUserInfo(null);
        review.setUser(user);

        when(modelMapper.map(any(Review.class), any())).thenReturn(reviewDTO);

        ReviewDTO resultDTO = reviewConverter.convert(review);

        assertEquals("John", resultDTO.getFirstName(), "FirstName should remain unchanged when UserInfo is absent");
        assertEquals("Doe", resultDTO.getLastName(), "LastName should remain unchanged when UserInfo is absent");
    }

    @Test
    void shouldConvertReviewDTOToReviewWhenContentIsPresent() {
        String expectedContent = "This is a review";
        reviewDTO.setContent(expectedContent);

        Review resultReview = reviewConverter.convert(reviewDTO);

        assertEquals(expectedContent, resultReview.getContent(), "Content should match the ReviewDTO content when present.");
    }

    @Test
    void shouldHandleNullContentCorrectlyWhenConvertingToReview() {
        reviewDTO.setContent(null);

        Review resultReview = reviewConverter.convert(reviewDTO);

        assertNull(resultReview.getContent(), "Content should remain null when it is null in ReviewDTO.");
    }

    @Test
    void shouldHandleAbsentUserFieldsCorrectly() {
        review.setUser(null);

        ReviewDTO expectedDTO = new ReviewDTO();
        expectedDTO.setFirstName(null);
        expectedDTO.setLastName(null);

        when(modelMapper.map(any(Review.class), eq(ReviewDTO.class))).thenReturn(expectedDTO);

        ReviewDTO resultDTO = reviewConverter.convert(review);

        assertNull(resultDTO.getFirstName(), "FirstName should be null when User is absent");
        assertNull(resultDTO.getLastName(), "LastName should be null when User is absent");
    }

    @Test
    void shouldSetFormattedDateOfCreationWhenDateIsNotNull() {
        LocalDateTime dateOfCreation = LocalDateTime.of(2024, 3, 20, 12, 0);
        review.setDateOfCreation(dateOfCreation);
        String expectedFormattedDate = "2024-03-20 12:00:00";

        when(modelMapper.map(any(Review.class), eq(ReviewDTO.class))).thenReturn(new ReviewDTO());

        ReviewDTO result = reviewConverter.convert(review);

        assertEquals(expectedFormattedDate, result.getDateOfCreation(), "The formatted date of creation should be set correctly in the ReviewDTO.");
    }

    @Test
    void shouldSetReviewStatusWhenReviewStatusIsNotNull() {
        ReviewStatus reviewStatus = new ReviewStatus();
        reviewStatus.setStatusName("Approved");
        review.setReviewStatus(reviewStatus);

        when(modelMapper.map(any(Review.class), eq(ReviewDTO.class))).thenReturn(new ReviewDTO());

        ReviewDTO result = reviewConverter.convert(review);

        assertEquals("Approved", result.getStatus(), "The review status should be set correctly in the ReviewDTO.");
    }

    @Test
    void shouldHandleNullDateOfCreationCorrectly() {
        review.setDateOfCreation(null);

        when(modelMapper.map(any(Review.class), any())).thenReturn(new ReviewDTO());

        ReviewDTO resultDTO = reviewConverter.convert(review);

        assertNull(resultDTO.getDateOfCreation(), "The dateOfCreation should be null in ReviewDTO when it's null in Review.");
    }

    @Test
    void shouldHandleNullReviewStatusCorrectly() {
        review.setReviewStatus(null);

        when(modelMapper.map(any(Review.class), any())).thenReturn(new ReviewDTO());

        ReviewDTO resultDTO = reviewConverter.convert(review);

        assertNull(resultDTO.getStatus(), "The status should be null in ReviewDTO when reviewStatus is null in Review.");
    }
}
