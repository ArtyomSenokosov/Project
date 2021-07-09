package ru.mail.senokosov.artem.service.model.add;

import lombok.Data;
import ru.mail.senokosov.artem.service.model.enums.RoleDTOEnum;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static ru.mail.senokosov.artem.service.constant.UserValidationConstant.*;

@Data
public class AddUserDTO {

    private Long id;
    @NotBlank
    @NotNull
    @Size(max = MAXIMUM_LAST_NAME_SIZE)
    @Pattern(regexp = ONLY_LATIN_LETTERS_REGEXP)
    private String lastName;
    @NotBlank
    @NotNull
    @Size(max = MAXIMUM_FIRST_NAME_SIZE)
    @Pattern(regexp = ONLY_LATIN_LETTERS_REGEXP)
    private String firstName;
    @NotBlank
    @NotNull
    @Size(max = MAXIMUM_MIDDLE_NAME_SIZE)
    @Pattern(regexp = ONLY_LATIN_LETTERS_REGEXP)
    private String middleName;
    @NotBlank
    @NotNull
    @Size(max = MAXIMUM_EMAIL_NAME_SIZE)
    @Pattern(regexp = EMAIL_REGEXP)
    private String email;
    @Enumerated(EnumType.STRING)
    private RoleDTOEnum role;
    @NotBlank
    @NotNull
    @Size(max = MAXIMUM_ADDRESS_SIZE)
    private String address;
    @NotBlank
    @NotNull
    @Size(max = MAXIMUM_TELEPHONE_SIZE)
    private String telephone;
}