package ru.mail.senokosov.artem.service.model;

import lombok.Data;
import ru.mail.senokosov.artem.repository.model.OrderStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class OrderDTO {

    private Long id;
    @NotBlank
    @NotNull
    private OrderStatus orderStatus;
}