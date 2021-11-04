package ru.mail.senokosov.artem.service.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderDTO {

    private Long id;
    private UUID uuidOfOrder;
    private String orderStatus;
    private String title;
    private Long numberOfItems;
    private BigDecimal totalPrice;
    private String lastName;
    private String telephone;
}