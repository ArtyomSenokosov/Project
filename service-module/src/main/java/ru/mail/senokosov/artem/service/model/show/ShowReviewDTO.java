package ru.mail.senokosov.artem.service.model.show;

import lombok.Data;

@Data
public class ShowReviewDTO {

    private Long id;
    private String lastName;
    private String firstName;
    private String middleName;
    private String review;
    private String localDateTime;
    private String status;
}