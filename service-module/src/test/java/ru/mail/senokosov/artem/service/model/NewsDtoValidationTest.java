package ru.mail.senokosov.artem.service.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.mail.senokosov.artem.service.constant.NewsConstant.MAXIMUM_CHARS_FOR_FULL_CONTENT_FIELD;
import static ru.mail.senokosov.artem.service.constant.NewsConstant.MAXIMUM_CHARS_FOR_TITLE_FIELD;

class NewsDtoValidationTest {

    private Validator validator;

    @BeforeEach
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldPassValidationWhenTitleAndFullContentAreValid() {
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setTitle("Valid Title");
        newsDTO.setFullContent("Valid Full Content");

        Set<ConstraintViolation<NewsDTO>> violations = validator.validate(newsDTO);

        assertThat(violations).isEmpty();
    }

    @Test
    public void shouldFailValidationWhenTitleExceedsSizeLimit() {
        NewsDTO newsDTO = new NewsDTO();
        String longTitle = "a".repeat(MAXIMUM_CHARS_FOR_TITLE_FIELD + 1);
        newsDTO.setTitle(longTitle);
        newsDTO.setFullContent("Valid Full Content");

        Set<ConstraintViolation<NewsDTO>> violations = validator.validate(newsDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Title must be less than " + MAXIMUM_CHARS_FOR_TITLE_FIELD + " characters");
    }

    @Test
    public void shouldFailValidationWhenFullContentExceedsSizeLimit() {
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setTitle("Valid Title");
        String longFullContent = "a".repeat(MAXIMUM_CHARS_FOR_FULL_CONTENT_FIELD + 1);
        newsDTO.setFullContent(longFullContent);

        Set<ConstraintViolation<NewsDTO>> violations = validator.validate(newsDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Full content must be less than " + MAXIMUM_CHARS_FOR_FULL_CONTENT_FIELD + " characters");
    }

    @Test
    public void shouldFailValidationWhenTitleIsNull() {
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setTitle(null);
        newsDTO.setFullContent("Valid Full Content");

        Set<ConstraintViolation<NewsDTO>> violations = validator.validate(newsDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Title cannot be null");
    }

    @Test
    public void shouldFailValidationWhenFullContentIsNull() {
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setTitle("Valid Title");
        newsDTO.setFullContent(null);

        Set<ConstraintViolation<NewsDTO>> violations = validator.validate(newsDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Full content cannot be null");
    }
}