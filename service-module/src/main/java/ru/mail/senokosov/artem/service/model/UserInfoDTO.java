package ru.mail.senokosov.artem.service.model;

import liquibase.pro.packaged.S;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static ru.mail.senokosov.artem.service.constant.UserServiceConstants.MAXIMUM_LAST_NAME_SIZE;
import static ru.mail.senokosov.artem.service.constant.UserValidationConstant.*;

@Data
public class UserInfoDTO {

    private Long id;
    @NotBlank
    @NotNull
    @Size(max = MAXIMUM_FIRST_NAME_SIZE)
    @Pattern(regexp = ONLY_LATIN_LETTERS_REGEXP, message = "The first name must be written in Latin letters and start with a capital letter")
    private String firstName;
    @NotBlank
    @NotNull
    @Size(max = MAXIMUM_LAST_NAME_SIZE)
    @Pattern(regexp = ONLY_LATIN_LETTERS_REGEXP, message = "The last name must be written in Latin letters and start with a capital letter")
    private String lastName;
    @NotBlank
    @NotNull
    @Size(max = MAXIMUM_TELEPHONE_SIZE)
    private String telephone;
    @NotBlank
    @NotNull
    @Size(max = MAXIMUM_ADDRESS_SIZE)
    private String address;
    @NotBlank
    @NotNull
    private String oldPassword;
    @NotBlank
    @NotNull
    private String newPassword;
}
