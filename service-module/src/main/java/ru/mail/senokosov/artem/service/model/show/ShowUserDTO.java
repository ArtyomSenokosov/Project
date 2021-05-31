package ru.mail.senokosov.artem.service.model.show;

import lombok.Data;

@Data
public class ShowUserDTO {

    private Long id;
    private String secondname;
    private String firstname;
    private String middlename;
    private String email;
    private String role;
}
