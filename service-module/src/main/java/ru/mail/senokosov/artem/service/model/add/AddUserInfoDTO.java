package ru.mail.senokosov.artem.service.model.add;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static ru.mail.senokosov.artem.service.constant.UserValidationConstant.*;

@Data
public class AddUserInfoDTO {

    private Long id;
    @NotNull
    @Size(max = MAXIMUM_FIRST_NAME_SIZE)
    private String firstName;
    @NotNull
    @Size(max = MAXIMUM_LAST_NAME_SIZE)
    private String lastName;
    @NotNull
    @Size(max = MAXIMUM_TELEPHONE_SIZE)
    private String telephone;
    @NotNull
    @Size(max = MAXIMUM_ADDRESS_SIZE)
    private String address;
    @NotNull
    private String oldPassword;
    @NotNull
    private String newPassword;
}