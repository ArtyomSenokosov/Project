package ru.mail.senokosov.artem.service.model.show;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ShowOrderDTO {

    private Long id;
    private Long orderNumber;
    private String status;
    private List<ShowItemDTO> items = new ArrayList<>();
    private Long quantity;
    private Long totalPrice;
    private String email;
    private String telephone;
}
