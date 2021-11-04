package ru.mail.senokosov.artem.service.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

import static ru.mail.senokosov.artem.service.constant.NewsConstant.MAXIMUM_CHARS_FOR_FULL_CONTENT_FIELD;
import static ru.mail.senokosov.artem.service.constant.NewsConstant.MAXIMUM_CHARS_FOR_TITLE_FIELD;

@Data
public class NewsDTO {

    private Long id;
    private String dateOfCreation;
    @NotNull(message = "Title cannot be null")
    @Size(max = MAXIMUM_CHARS_FOR_TITLE_FIELD, message = "Title must be less than {max} characters")
    private String title;
    private String shortContent;
    @NotNull(message = "Full content cannot be null")
    @Size(max = MAXIMUM_CHARS_FOR_FULL_CONTENT_FIELD, message = "Full content must be less than {max} characters")
    private String fullContent;
    private String lastName;
    private String firstName;
    private List<CommentDTO> comments = new ArrayList<>();
}