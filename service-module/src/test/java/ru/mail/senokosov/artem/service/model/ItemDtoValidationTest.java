package ru.mail.senokosov.artem.service.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.mail.senokosov.artem.service.constant.ItemConstant.MAXIMUM_CHARS_FOR_TITLE_FIELD_TO_ITEM;

class ItemDtoValidationTest {

    private Validator validator;

    @BeforeEach
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldPassValidationWhenTitleAndPriceAreValid() {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setTitle("Valid Title");
        itemDTO.setContent("Valid content");
        itemDTO.setPrice(new BigDecimal("10.00"));

        Set<ConstraintViolation<ItemDTO>> violations = validator.validate(itemDTO);

        assertThat(violations).isEmpty();
    }

    @Test
    public void shouldFailValidationWhenTitleIsNull() {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setTitle(null);
        itemDTO.setContent("Valid content");
        itemDTO.setPrice(new BigDecimal("10.00"));

        Set<ConstraintViolation<ItemDTO>> violations = validator.validate(itemDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Title cannot be null");
    }

    @Test
    public void shouldFailValidationWhenTitleExceedsSizeLimit() {
        ItemDTO itemDTO = new ItemDTO();
        String longTitle = "a".repeat(MAXIMUM_CHARS_FOR_TITLE_FIELD_TO_ITEM + 1);
        itemDTO.setTitle(longTitle);
        itemDTO.setContent("Valid content");
        itemDTO.setPrice(new BigDecimal("10.00"));

        Set<ConstraintViolation<ItemDTO>> violations = validator.validate(itemDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Title must be less than " + MAXIMUM_CHARS_FOR_TITLE_FIELD_TO_ITEM + " characters");
    }

    @Test
    public void shouldFailValidationWhenPriceIsNotPositive() {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setTitle("Valid Title");
        itemDTO.setContent("Valid content");
        itemDTO.setPrice(new BigDecimal("-1.00"));

        Set<ConstraintViolation<ItemDTO>> violations = validator.validate(itemDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Price must be a positive number");
    }
}
