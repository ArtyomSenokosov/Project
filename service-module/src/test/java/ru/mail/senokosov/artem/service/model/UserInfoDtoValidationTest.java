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
import static ru.mail.senokosov.artem.service.constant.UserValidationConstant.MAXIMUM_ADDRESS_SIZE;

public class UserInfoDtoValidationTest {

    private Validator validator;

    @BeforeEach
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldPassValidationWhenTelephoneNumberIsValid() {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setTelephone("1234567890");

        Set<ConstraintViolation<UserInfoDTO>> violations = validator.validate(userInfoDTO);

        assertTrue(violations.isEmpty(), "There should be no violations when telephone format is valid.");
    }

    @Test
    public void shouldFailValidationWhenTelephoneNumberIsInvalid() {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setTelephone("invalid number");

        Set<ConstraintViolation<UserInfoDTO>> violations = validator.validate(userInfoDTO);

        assertEquals(1, violations.size(), "There should be exactly one violation when telephone format is invalid.");
        assertTrue(violations.iterator().next().getMessage().contains("Telephone format is invalid"), "The violation message should match the expected one.");
    }

    @Test
    public void shouldFailValidationWhenAddressExceedsMaxLength() {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        String longAddress = "a".repeat(MAXIMUM_ADDRESS_SIZE + 1);
        userInfoDTO.setAddress(longAddress);

        Set<ConstraintViolation<UserInfoDTO>> violations = validator.validate(userInfoDTO);

        assertEquals(1, violations.size(), "There should be exactly one violation when address exceeds maximum length.");
        assertTrue(violations.iterator().next().getMessage().contains("Address must be less than"), "The violation message should indicate that address exceeds size limit.");
    }
}
