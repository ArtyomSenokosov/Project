package ru.mail.senokosov.artem.service.model.show;

import lombok.Data;

@Data
public class ShowCommentDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String date;
    private String fullContent;
}
