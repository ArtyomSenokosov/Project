package ru.mail.senokosov.artem.service.converter.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.mail.senokosov.artem.repository.model.Item;
import ru.mail.senokosov.artem.repository.model.ItemInfo;
import ru.mail.senokosov.artem.service.model.add.AddItemDTO;
import ru.mail.senokosov.artem.service.model.show.ShowItemDTO;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ItemConverterImplTest {

    @InjectMocks
    private ItemConverterImpl itemConverter;

    @Test
    void shouldConvertItemToShowItemDTOAndReturnRightId() {
        Item item = new Item();
        Long id = 1L;
        item.setId(id);
        ShowItemDTO showItemDTO = itemConverter.convert(item);

        assertEquals(id, showItemDTO.getId());
    }

    @Test
    void shouldConvertItemToShowItemDTOAndReturnRightTitle() {
        Item item = new Item();
        String title = "test title";
        item.setTitle(title);
        ShowItemDTO showItemDTO = itemConverter.convert(item);

        assertEquals(title, showItemDTO.getTitle());
    }

    @Test
    void shouldConvertItemToShowItemDTOAndReturnRightUuid() {
        Item item = new Item();
        UUID uuid = UUID.fromString("de05425c-da35-45ba-be2f-61284704662e");
        item.setUuid(uuid);
        ShowItemDTO showItemDTO = itemConverter.convert(item);

        assertEquals(uuid, showItemDTO.getUuid());
    }

    @Test
    void shouldConvertItemToShowItemDTOAndReturnRightPrice() {
        Item item = new Item();
        BigDecimal price = BigDecimal.valueOf(100);
        item.setPrice(price);
        ShowItemDTO showItemDTO = itemConverter.convert(item);

        assertEquals(price, showItemDTO.getPrice());
    }

    @Test
    void shouldConvertItemToShowItemDTOAndReturnRightContent() {
        ItemInfo itemInfo = new ItemInfo();
        String content = "test content";
        itemInfo.setShortContent(content);
        Item item = new Item();
        item.setItemInfo(itemInfo);
        ShowItemDTO showItemDTO = itemConverter.convert(item);

        assertEquals(content, showItemDTO.getContent());
    }

    @Test
    void shouldConvertAddItemDTOToItemAndReturnRightTitle() {
        AddItemDTO addItemDTO = new AddItemDTO();
        String title = "test title";
        addItemDTO.setTitle(title);
        Item item = itemConverter.convert(addItemDTO);

        assertEquals(title, item.getTitle());
    }

    @Test
    void shouldConvertAddItemDTOToItemAndReturnRightPrice() {
        AddItemDTO addItemDTO = new AddItemDTO();
        BigDecimal price = BigDecimal.valueOf(100);
        addItemDTO.setPrice(price);
        Item item = itemConverter.convert(addItemDTO);

        assertEquals(price, item.getPrice());
    }

    @Test
    void shouldConvertAddItemDTOToItemAndReturnRightContent() {
        AddItemDTO addItemDTO = new AddItemDTO();
        String content = "test content";
        addItemDTO.setContent(content);
        Item item = itemConverter.convert(addItemDTO);

        assertEquals(content, item.getItemInfo().getShortContent());
    }
}