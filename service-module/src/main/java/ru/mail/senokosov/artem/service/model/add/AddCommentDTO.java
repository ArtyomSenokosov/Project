package ru.mail.senokosov.artem.service.model.add;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static ru.mail.senokosov.artem.service.constant.CommentValidationConstant.MAXIMUM_FULL_CONTENT_SIZE;

@Data
public class AddCommentDTO {

    @NotBlank
    @NotNull
    @Size(max = MAXIMUM_FULL_CONTENT_SIZE)
    private String fullContent;
}
