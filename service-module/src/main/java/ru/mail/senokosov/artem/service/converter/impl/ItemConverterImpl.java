package ru.mail.senokosov.artem.service.converter.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import ru.mail.senokosov.artem.repository.model.Item;
import ru.mail.senokosov.artem.service.converter.ItemConverter;
import ru.mail.senokosov.artem.service.model.add.AddItemDTO;
import ru.mail.senokosov.artem.service.model.show.ShowItemDTO;

import java.util.UUID;

@Component
@Log4j2
@RequiredArgsConstructor
public class ItemConverterImpl implements ItemConverter {
    @Override
    public ShowItemDTO convert(Item item) {
        ShowItemDTO showItemDTO = new ShowItemDTO();
        Long id = item.getId();
        showItemDTO.setId(id);
        String name = item.getName();
        showItemDTO.setName(name);
        UUID uuid = item.getUuid();
        showItemDTO.setUuid(uuid);
        Long price = item.getPrice();
        showItemDTO.setPrice(price);
        Long number = item.getNumber();
        showItemDTO.setNumber(number);
        String content = item.getContent();
        showItemDTO.setContent(content);
        return showItemDTO;
    }

    @Override
    public Item convert(AddItemDTO addItemDTO) {
        Item item = new Item();
        String name = addItemDTO.getName();
        item.setName(name);
        UUID uuid = addItemDTO.getUuid();
        item.setUuid(uuid);
        Long price = addItemDTO.getPrice();
        item.setPrice(price);
        Long number = addItemDTO.getNumber();
        item.setNumber(number);
        String content = addItemDTO.getContent();
        item.setContent(content);
        return item;
    }
}
