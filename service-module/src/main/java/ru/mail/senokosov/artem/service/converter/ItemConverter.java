package ru.mail.senokosov.artem.service.converter;

import ru.mail.senokosov.artem.repository.model.Item;
import ru.mail.senokosov.artem.service.model.ItemDTO;

public interface ItemConverter {

    ItemDTO convert(Item item);

    Item convert(ItemDTO itemDTO);
}