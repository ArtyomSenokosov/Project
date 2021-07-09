package ru.mail.senokosov.artem.service.model.show;

import lombok.Data;

@Data
public class ShowUserDTO {

    private Long id;
    private String lastName;
    private String firstName;
    private String middleName;
    private String email;
    private String roleName;
    private String password;
    private String encryptedPassword;
}