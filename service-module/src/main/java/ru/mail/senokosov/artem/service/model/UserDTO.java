package ru.mail.senokosov.artem.service.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

import static ru.mail.senokosov.artem.service.constant.UserValidationConstant.*;

@Data
public class UserDTO {

    private Long id;
    private UUID uuid;
    @NotNull(message = "Last name cannot be null")
    @Size(min = MINIMUM_LAST_NAME_SIZE, max = MAXIMUM_LAST_NAME_SIZE,
            message = "Last name must be in the range between {min} and {max}")
    private String lastName;
    @NotNull(message = "First name cannot be null")
    @Size(min = MINIMUM_FIRST_NAME_SIZE, max = MAXIMUM_FIRST_NAME_SIZE,
            message = "First name must be in the range between {min} and {max}}")
    private String firstName;
    @Size(min = MINIMUM_MIDDLE_NAME_SIZE, max = MAXIMUM_MIDDLE_NAME_SIZE,
            message = "Middle name must be in the range between {min} and {max}")
    private String middleName;
    @NotNull(message = "Email cannot be null")
    @Size(min = MINIMUM_EMAIL_NAME_SIZE, max = MAXIMUM_EMAIL_NAME_SIZE,
            message = "Email must be in the range between {min} and {max}")
    @Pattern(regexp = EMAIL_REGEXP)
    private String email;
    private String password;
    private String roleName;
}