package ru.mail.senokosov.artem.service.model.show;

import lombok.Data;

import java.util.UUID;

@Data
public class ShowItemDTO {

    private Long id;
    private String name;
    private UUID uuid;
    private Long price;
    private Long number;
    private String content;
}
