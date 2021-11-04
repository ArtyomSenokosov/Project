package ru.mail.senokosov.artem.service.model;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

import static ru.mail.senokosov.artem.service.constant.ItemConstant.*;

@Data
public class ItemDTO {

    private Long id;
    private UUID uuid;
    @NotNull(message = "Title cannot be null")
    @Size(max = MAXIMUM_CHARS_FOR_TITLE_FIELD_TO_ITEM, message = "Title must be less than {max} characters")
    private String title;
    @Size(max = MAXIMUM_CHARS_FOR_CONTENT_FIELD_TO_ITEM, message = "Content must be less than {max} characters")
    private String content;
    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = MINIMUM_PRICE_SIZE, message = "Price must be a positive number")
    private BigDecimal price;
}