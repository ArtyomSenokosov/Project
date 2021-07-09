package ru.mail.senokosov.artem.service.model.add;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.math.BigDecimal;

import static ru.mail.senokosov.artem.service.constant.ItemConstant.*;

@Data
public class AddItemDTO {

    @NotBlank
    @NotNull
    @Size(max = MAXIMUM_CHARS_FOR_TITLE_FIELD_TO_ITEM, message = "Title must be between 1 and 40 characters")
    private String title;
    @DecimalMin(value = MINIMUM_PRICE_SIZE, inclusive = false)
    private BigDecimal price;
    @NotBlank
    @NotNull
    @Size(max = MAXIMUM_CHARS_FOR_CONTENT_FIELD_TO_ITEM, message = "Short content must be between 1 and 200 characters")
    private String content;
}