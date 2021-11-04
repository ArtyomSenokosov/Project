package ru.mail.senokosov.artem.service.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static ru.mail.senokosov.artem.service.constant.ReviewConstant.MAXIMUM_CHARS_FOR_FULL_CONTENT_FIELD;

@Data
public class ReviewDTO {

    private Long id;
    @NotNull(message = "Content cannot be null")
    @Size(max = MAXIMUM_CHARS_FOR_FULL_CONTENT_FIELD, message = "Content must be less than {max} characters")
    private String content;
    private String dateOfCreation;
    private String status;
    private String lastName;
    private String firstName;
}