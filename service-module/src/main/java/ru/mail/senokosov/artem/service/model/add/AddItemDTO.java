package ru.mail.senokosov.artem.service.model.add;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.UUID;

import static ru.mail.senokosov.artem.service.constant.ItemConstant.MAXIMUM_CHARS_FOR_ITEM_DESCRIPTION;

@Data
public class AddItemDTO {


    private Long id;
    @NotBlank
    @NotNull
    private String name;
    @NotBlank
    @NotNull
    private UUID uuid;
    @NotBlank
    private Long price;
    @NotBlank
    private Long number;
    @NotBlank
    @NotNull
    @Size(max = MAXIMUM_CHARS_FOR_ITEM_DESCRIPTION, message = "Description must be between 1 and 200 characters")
    private String content;
    private Long userId;
}
