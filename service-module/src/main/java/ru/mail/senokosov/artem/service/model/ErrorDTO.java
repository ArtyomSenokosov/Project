package ru.mail.senokosov.artem.service.model;

import lombok.Data;
import org.springframework.validation.FieldError;

import java.lang.reflect.Field;
import java.util.List;

@Data
public class ErrorDTO {

    private List<FieldError> errors;
}
