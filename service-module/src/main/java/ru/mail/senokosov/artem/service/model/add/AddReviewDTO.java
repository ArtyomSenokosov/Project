package ru.mail.senokosov.artem.service.model.add;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddReviewDTO {

    private Long id;
    @NotBlank
    @NotNull
    private String topic;
    @NotBlank
    @NotNull
    private String review;
    private Long userId;
}
