package ru.mail.senokosov.artem.service.converter;

import ru.mail.senokosov.artem.repository.model.Item;
import ru.mail.senokosov.artem.service.model.add.AddItemDTO;
import ru.mail.senokosov.artem.service.model.show.ShowItemDTO;

public interface ItemConverter {

    ShowItemDTO convert(Item item);

    Item convert(AddItemDTO addItemDTO);
}
