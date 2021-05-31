package ru.mail.senokosov.artem.service.model.add;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static ru.mail.senokosov.artem.service.constant.ArticleConstant.MAXIMUM_CHARS_FOR_TITLE_FIELD;

@Data
public class AddArticleDTO {

    private Long id;
    @NotBlank
    @NotNull
    @Size(max = MAXIMUM_CHARS_FOR_TITLE_FIELD, message = "Title must be between 1 and 20 characters")
    private String title;
    @NotBlank
    @NotNull
    @Size(max = MAXIMUM_CHARS_FOR_TITLE_FIELD, message = "Full content must be between 1 and 1000 characters")
    private String content;
    private Long userId;
}
