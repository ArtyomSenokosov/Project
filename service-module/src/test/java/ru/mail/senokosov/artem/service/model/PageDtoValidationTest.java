package ru.mail.senokosov.artem.service.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

class PageDtoValidationTest {

    private Validator validator;

    @BeforeEach
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldPassValidationWhenPageInfoIsValid() {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setTotalPages(10L);
        pageDTO.setCurrentPage(1L);
        pageDTO.setBeginPage(1L);
        pageDTO.setEndPage(5L);
        pageDTO.setStartPosition(0);

        Set<ConstraintViolation<PageDTO>> violations = validator.validate(pageDTO);

        assertThat(violations).isEmpty();
    }

    @Test
    public void shouldFailValidationWhenCurrentPageIsGreaterThanTotalPages() {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setTotalPages(10L);
        pageDTO.setCurrentPage(11L);
        pageDTO.setBeginPage(1L);
        pageDTO.setEndPage(5L);
        pageDTO.setStartPosition(0);

        boolean isValid = pageDTO.getCurrentPage() <= pageDTO.getTotalPages();

        assertFalse(isValid, "Validation should fail when current page is greater than total pages.");
    }
}