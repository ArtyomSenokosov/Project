package ru.mail.senokosov.artem.service.model.show;

import lombok.Data;

@Data
public class ShowUserInfoDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String telephone;
    private String address;
    private String oldPassword;
    private String newPassword;
}