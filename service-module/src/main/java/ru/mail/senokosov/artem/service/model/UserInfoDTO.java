package ru.mail.senokosov.artem.service.model;

import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static ru.mail.senokosov.artem.service.constant.UserValidationConstant.*;

@Data
public class UserInfoDTO {

    private Long id;
    private String firstName;
    private String lastName;
    @Pattern(regexp = ONLY_LATIN_LETTERS_REGEXP, message = "Telephone format is invalid")
    @Size(max = MAXIMUM_TELEPHONE_SIZE, message = "Telephone must be less than {max} characters")
    private String telephone;
    @Size(max = MAXIMUM_ADDRESS_SIZE, message = "Address must be less than {max} characters")
    private String address;
    private String oldPassword;
    private String newPassword;
}