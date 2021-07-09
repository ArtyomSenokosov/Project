package ru.mail.senokosov.artem.service.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Data
public class ArticleDTO {

    private Long id;
    @NotBlank
    @NotNull
    private LocalDateTime localDateTime;
    @NotBlank
    @NotNull
    private String title;
    @NotBlank
    @NotNull
    private String fullContent;
}