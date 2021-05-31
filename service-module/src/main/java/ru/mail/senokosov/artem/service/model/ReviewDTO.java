package ru.mail.senokosov.artem.service.model;

import liquibase.pro.packaged.S;
import lombok.Data;
import ru.mail.senokosov.artem.repository.model.ReviewStatus;
import ru.mail.senokosov.artem.repository.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class ReviewDTO {

    private Long id;
    @NotBlank
    @NotNull
    private ReviewStatus status;

}
