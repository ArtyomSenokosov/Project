package ru.mail.senokosov.artem.service.model.show;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ShowItemDTO {

    private Long id;
    private String title;
    private UUID uuid;
    private BigDecimal price;
    private String content;
}