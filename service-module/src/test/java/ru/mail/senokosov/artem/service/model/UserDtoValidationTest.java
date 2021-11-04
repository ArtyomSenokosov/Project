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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.mail.senokosov.artem.service.constant.UserValidationConstant.MAXIMUM_FIRST_NAME_SIZE;

class UserDtoValidationTest {

    private Validator validator;

    @BeforeEach
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldPassValidationWhenUserDTOIsValid() {
        UserDTO userDTO = new UserDTO();
        userDTO.setLastName("Doe");
        userDTO.setFirstName("John");
        userDTO.setMiddleName("Edward");
        userDTO.setEmail("john.doe@example.com");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);

        assertThat(violations).isEmpty();
    }

    @Test
    public void shouldFailValidationWhenLastNameIsNull() {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("John");
        userDTO.setEmail("john.doe@example.com");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Last name cannot be null");
    }

    @Test
    public void shouldFailValidationWhenFirstNameExceedsMaxLength() {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("J".repeat(MAXIMUM_FIRST_NAME_SIZE + 1));
        userDTO.setLastName("Doe");
        userDTO.setEmail("john.doe@example.com");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).contains("First name must be in the range between");
    }

    @Test
    public void shouldFailValidationWhenEmailIsInvalid() {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setEmail("invalid-email");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);

        assertFalse(violations.isEmpty(), "There should be validation errors.");

        boolean hasEmailPatternError = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")
                        && v.getConstraintDescriptor().getAnnotation().annotationType().equals(javax.validation.constraints.Pattern.class));
        assertTrue(hasEmailPatternError, "There should be a pattern violation message for the email field.");
    }
}