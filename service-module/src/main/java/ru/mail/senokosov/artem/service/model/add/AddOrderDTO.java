package ru.mail.senokosov.artem.service.model.add;

import lombok.Data;

@Data
public class AddOrderDTO {

    private Long id;
    private Long itemId;
    private Long quantity;
    private Long totalPrice;
    private Long userId;

}
