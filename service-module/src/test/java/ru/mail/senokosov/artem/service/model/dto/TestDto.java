package ru.mail.senokosov.artem.service.model.dto;

import javax.validation.constraints.Size;

public class TestDto {

    @Size(min = 1, max = 10)
    private String shortText;

    @Size(min = 5, max = 255)
    private String longText;

    private String noSizeText;
}
