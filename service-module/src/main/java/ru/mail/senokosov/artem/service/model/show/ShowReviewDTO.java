package ru.mail.senokosov.artem.service.model.show;

import lombok.Data;

@Data
public class ShowReviewDTO {

    private Long id;
    private String firstname;
    private String secondname;
    private String middlename;
    private String date;
    private String topic;
    private String review;

}
