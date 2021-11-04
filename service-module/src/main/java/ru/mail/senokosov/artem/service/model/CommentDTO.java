package ru.mail.senokosov.artem.service.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static ru.mail.senokosov.artem.service.constant.CommentConstant.MAXIMUM_FULL_CONTENT_SIZE;

@Data
public class CommentDTO {

    private Long id;
    private String lastName;
    private String firstName;
    private String dateOfCreation;
    @NotNull(message = "Content cannot be null")
    @Size(max = MAXIMUM_FULL_CONTENT_SIZE, message = "Content must be less than {max} characters")
    private String fullContent;
}
