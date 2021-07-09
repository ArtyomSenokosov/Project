package ru.mail.senokosov.artem.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.mail.senokosov.artem.repository.ItemRepository;
import ru.mail.senokosov.artem.repository.model.Item;
import ru.mail.senokosov.artem.service.converter.ItemConverter;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.PageDTO;
import ru.mail.senokosov.artem.service.model.add.AddItemDTO;
import ru.mail.senokosov.artem.service.model.show.ShowItemDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemConverter itemConverter;
    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void shouldGetItemsByNumberPage() {
        int startPosition = 0;
        int maximumItemsOnPage = 10;
        List<Item> items = new ArrayList<>();
        when(itemRepository.findAll(startPosition, maximumItemsOnPage)).thenReturn(items);
        List<ShowItemDTO> itemDTOS = items.stream()
                .map(itemConverter::convert)
                .collect(Collectors.toList());
        PageDTO pageDTO = new PageDTO();
        pageDTO.getItems().addAll(itemDTOS);

        PageDTO itemsByPage = itemService.getItemsByPage(1);

        assertEquals(pageDTO.getItems(), itemsByPage.getItems());
    }

    @Test
    void shouldGetAllItems() {
        List<Item> itemsWithMock = new ArrayList<>();
        when(itemRepository.findAll()).thenReturn(itemsWithMock);
        List<ShowItemDTO> itemDTOSWithMock = itemsWithMock.stream()
                .map(itemConverter::convert)
                .collect(Collectors.toList());

        List<Item> items = itemRepository.findAll();
        List<ShowItemDTO> itemDTOS = items.stream()
                .map(itemConverter::convert)
                .collect(Collectors.toList());

        assertEquals(itemDTOSWithMock, itemDTOS);
    }

    @Test
    void shouldFindItemByIdAndReturnExceptionIfItemNotFound() {
        Long id = 1L;
        assertThrows(ServiceException.class, () -> itemService.getItemById(id));
    }

    @Test
    void shouldGetItemById() throws ServiceException {
        Long id = 1L;
        Item item = new Item();
        item.setId(id);
        when(itemRepository.findById(id)).thenReturn(item);
        ShowItemDTO showItemDTO = new ShowItemDTO();
        showItemDTO.setId(id);
        when(itemConverter.convert(item)).thenReturn(showItemDTO);

        ShowItemDTO itemById = itemService.getItemById(id);

        assertEquals(showItemDTO, itemById);
    }

    @Test
    void shouldAddItem() {
        AddItemDTO addItemDTO = new AddItemDTO();
        addItemDTO.setTitle("title");
        addItemDTO.setContent("content");
        addItemDTO.setPrice(BigDecimal.valueOf(100));
        Item item = new Item();
        when(itemConverter.convert(addItemDTO)).thenReturn(item);
        item.setUuid(UUID.fromString("de05425c-da35-45ba-be2f-61284704662e"));
        ShowItemDTO showItemDTO = new ShowItemDTO();
        when(itemConverter.convert(item)).thenReturn(showItemDTO);

        ShowItemDTO showItem = itemService.persist(addItemDTO);
        assertEquals(showItem, showItemDTO);
    }

    @Test
    void shouldDeleteItemById() {
        Long id = 1L;
        boolean isDeleteItem = itemService.isDeleteById(id);

        assertTrue(isDeleteItem);
    }

    @Test
    void shouldFindItemByUuidAndReturnExceptionIfItemWasNotFound() {
        UUID uuid = UUID.fromString("de05425c-da35-45ba-be2f-61284704662e");
        assertThrows(ServiceException.class, () -> itemService.getItemByUuid(uuid));
    }

    @Test
    void shouldGetItemByUuid() throws ServiceException {
        UUID uuid = UUID.fromString("de05425c-da35-45ba-be2f-61284704662e");
        Item item = new Item();
        when(itemRepository.findByUuid(uuid)).thenReturn(item);
        ShowItemDTO showItemDTO = new ShowItemDTO();
        when(itemConverter.convert(item)).thenReturn(showItemDTO);
        ShowItemDTO itemByUuid = itemService.getItemByUuid(uuid);

        assertEquals(showItemDTO, itemByUuid);
    }

    @Test
    void shouldDeleteItemByUuidAndReturnTrueIfItemDeletedSuccessfully() throws ServiceException {
        UUID uuid = UUID.fromString("de05425c-da35-45ba-be2f-61284704662e");
        Item item = new Item();
        when(itemRepository.findByUuid(uuid)).thenReturn(item);
        boolean isDeleteByUuid = itemService.isDeleteByUuid(uuid);

        assertTrue(isDeleteByUuid);
    }

    @Test
    void shouldDeleteItemByUuidAndReturnExceptionIfItemByUuidWasNotFound() {
        UUID uuid = UUID.fromString("de05425c-da35-45ba-be2f-61284704662e");
        assertThrows(ServiceException.class, () -> itemService.isDeleteByUuid(uuid));
    }

    @Test
    void shouldCopyItemByUuidAndReturnNotEqualsTitleIfItemCopiedSuccessfully() throws ServiceException {
        UUID uuid = UUID.fromString("de05425c-da35-45ba-be2f-61284704662e");
        ShowItemDTO showItemDTO = new ShowItemDTO();
        showItemDTO.setTitle("title");
        Item item = new Item();
        item.setUuid(uuid);
        item.setTitle("title");
        when(itemRepository.findByUuid(uuid)).thenReturn(item);
        when(itemService.CopyItemByUuid(uuid)).thenReturn(showItemDTO);
        assertNotEquals(item.getTitle(), showItemDTO.getTitle());
    }

    @Test
    void shouldCopyItemByUuidAndReturnExceptionIfItemByUuidWasNotFound() {
        UUID uuid = UUID.fromString("de05425c-da35-45ba-be2f-61284704662e");
        assertThrows(ServiceException.class, () -> itemService.isDeleteByUuid(uuid));
    }
}