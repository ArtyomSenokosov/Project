package ru.mail.senokosov.artem.service.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.mail.senokosov.artem.service.constant.ReviewConstant.MAXIMUM_CHARS_FOR_FULL_CONTENT_FIELD;

public class ReviewDtoValidationTest {

    private Validator validator;

    @BeforeEach
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldPassValidationWhenContentIsValid() {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setContent("This is a valid content.");

        Set<ConstraintViolation<ReviewDTO>> violations = validator.validate(reviewDTO);

        assertTrue(violations.isEmpty(), "There should be no violations when content is valid.");
    }

    @Test
    public void shouldFailValidationWhenContentIsNull() {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setContent(null);

        Set<ConstraintViolation<ReviewDTO>> violations = validator.validate(reviewDTO);

        assertEquals(1, violations.size(), "There should be exactly one violation when content is null.");
        assertEquals("Content cannot be null", violations.iterator().next().getMessage(), "The violation message should match the expected one.");
    }

    @Test
    public void shouldFailValidationWhenContentExceedsSizeLimit() {
        ReviewDTO reviewDTO = new ReviewDTO();
        String longContent = "a".repeat(MAXIMUM_CHARS_FOR_FULL_CONTENT_FIELD + 1);
        reviewDTO.setContent(longContent);

        Set<ConstraintViolation<ReviewDTO>> violations = validator.validate(reviewDTO);

        assertEquals(1, violations.size(), "There should be exactly one violation when content exceeds size limit.");
        assertTrue(violations.iterator().next().getMessage().contains("Content must be less than"), "The violation message should indicate that content exceeds size limit.");
    }
}
