package ru.mail.senokosov.artem.service.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import javax.validation.ConstraintViolation;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.mail.senokosov.artem.service.constant.CommentConstant.MAXIMUM_FULL_CONTENT_SIZE;

public class CommentDtoValidationTest {

    private Validator validator;

    @BeforeEach
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldPassValidationWhenFullContentIsNotNullAndWithinSizeLimit() {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setFullContent("Valid content");

        Set<ConstraintViolation<CommentDTO>> violations = validator.validate(commentDTO);

        assertThat(violations).isEmpty();
    }

    @Test
    public void shouldFailValidationWhenFullContentIsNull() {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setFullContent(null);

        Set<ConstraintViolation<CommentDTO>> violations = validator.validate(commentDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Content cannot be null");
    }

    @Test
    public void shouldFailValidationWhenFullContentExceedsSizeLimit() {
        CommentDTO commentDTO = new CommentDTO();
        String longContent = "a".repeat(MAXIMUM_FULL_CONTENT_SIZE + 1);
        commentDTO.setFullContent(longContent);

        Set<ConstraintViolation<CommentDTO>> violations = validator.validate(commentDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Content must be less than " + MAXIMUM_FULL_CONTENT_SIZE + " characters");
    }
}
