package ru.mail.senokosov.artem.service.converter.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import ru.mail.senokosov.artem.repository.model.Item;
import ru.mail.senokosov.artem.service.model.ItemDTO;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ItemConverterImplTest {

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ItemConverterImpl itemConverter;

    private Item item;
    private ItemDTO itemDTO;

    @BeforeEach
    void setUp() {
        item = new Item();
        item.setTitle("Sample Item");
        item.setContent("This is a sample item content");
        item.setPrice(new BigDecimal("19.99"));

        itemDTO = new ItemDTO();
        itemDTO.setTitle("Sample Item");
        itemDTO.setContent("This is a sample item content");
        itemDTO.setPrice(new BigDecimal("19.99"));
    }

    @Test
    void shouldCorrectlyConvertItemToDTO() {
        when(modelMapper.map(item, ItemDTO.class)).thenReturn(itemDTO);
        ItemDTO resultDTO = itemConverter.convert(item);

        assertEquals("Sample Item", resultDTO.getTitle());
        assertEquals("This is a sample item content", resultDTO.getContent());
        assertEquals(new BigDecimal("19.99"), resultDTO.getPrice());
    }

    @Test
    void shouldCorrectlyConvertDTOToItem() {
        when(modelMapper.map(itemDTO, Item.class)).thenReturn(item);
        Item resultItem = itemConverter.convert(itemDTO);

        assertEquals("Sample Item", resultItem.getTitle());
        assertEquals("This is a sample item content", resultItem.getContent());
        assertEquals(new BigDecimal("19.99"), resultItem.getPrice());
    }
}
