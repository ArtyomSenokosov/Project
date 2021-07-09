package ru.mail.senokosov.artem.service.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrderItemDTO {

    @NotNull
    private Long numberOfItems;
}