package ru.mail.senokosov.artem.service.converter.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.mail.senokosov.artem.repository.model.Item;
import ru.mail.senokosov.artem.service.converter.ItemConverter;
import ru.mail.senokosov.artem.service.model.ItemDTO;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class ItemConverterImpl implements ItemConverter {

    private final ModelMapper modelMapper;

    @Override
    public ItemDTO convert(Item item) {
        ItemDTO itemDTO = modelMapper.map(item, ItemDTO.class);

        String shortContent = item.getContent();
        itemDTO.setContent(shortContent);

        return itemDTO;
    }

    @Override
    public Item convert(ItemDTO itemDTO) {
        Item item = new Item();

        String title = itemDTO.getTitle();
        item.setTitle(title);

        String content = itemDTO.getContent();
        item.setContent(content);

        BigDecimal price = itemDTO.getPrice();
        item.setPrice(price);

        return item;
    }
}