package ru.mail.senokosov.artem.service.converter.impl;

import org.springframework.stereotype.Component;
import ru.mail.senokosov.artem.repository.model.Item;
import ru.mail.senokosov.artem.repository.model.ItemInfo;
import ru.mail.senokosov.artem.service.converter.ItemConverter;
import ru.mail.senokosov.artem.service.model.add.AddItemDTO;
import ru.mail.senokosov.artem.service.model.show.ShowItemDTO;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Component
public class ItemConverterImpl implements ItemConverter {

    @Override
    public ShowItemDTO convert(Item item) {
        ShowItemDTO showItemDTO = new ShowItemDTO();
        Long id = item.getId();
        showItemDTO.setId(id);
        String title = item.getTitle();
        showItemDTO.setTitle(title);
        UUID uuid = item.getUuid();
        showItemDTO.setUuid(uuid);
        BigDecimal price = item.getPrice();
        showItemDTO.setPrice(price);
        ItemInfo itemInfo = item.getItemInfo();
        if (Objects.nonNull(itemInfo)) {
            String shortContent = itemInfo.getShortContent();
            showItemDTO.setContent(shortContent);
        }
        return showItemDTO;
    }

    @Override
    public Item convert(AddItemDTO addItemDTO) {
        Item item = new Item();
        String title = addItemDTO.getTitle();
        item.setTitle(title);
        BigDecimal price = addItemDTO.getPrice();
        item.setPrice(price);
        ItemInfo itemInfo = new ItemInfo();
        String content = addItemDTO.getContent();
        itemInfo.setShortContent(content);

        itemInfo.setItem(item);
        item.setItemInfo(itemInfo);
        return item;
    }
}