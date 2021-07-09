package ru.mail.senokosov.artem.service.model;

import lombok.Data;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Data
public class ErrorDTO {

    private List<FieldError> errors = new ArrayList<>();
}