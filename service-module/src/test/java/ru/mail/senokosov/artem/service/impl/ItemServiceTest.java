package ru.mail.senokosov.artem.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.mail.senokosov.artem.repository.ItemRepository;
import ru.mail.senokosov.artem.repository.OrderRepository;
import ru.mail.senokosov.artem.repository.model.Item;
import ru.mail.senokosov.artem.service.converter.ItemConverter;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.ItemDTO;
import ru.mail.senokosov.artem.service.model.PageDTO;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ItemConverter itemConverter;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void shouldReturnItemsByPageWhenRequested() {
        when(itemRepository.getCount()).thenReturn(10L);
        when(itemRepository.findAll(anyInt(), anyInt())).thenReturn(Collections.singletonList(new Item()));
        when(itemConverter.convert(any(Item.class))).thenReturn(new ItemDTO());

        PageDTO result = itemService.getItemsByPage(1);

        assertNotNull(result);
        assertEquals(1, result.getItems().size());

        verify(itemRepository, times(1)).getCount();
        verify(itemRepository, times(1)).findAll(anyInt(), anyInt());
    }

    @Test
    void shouldReturnItemDTOWhenFoundById() throws ServiceException {
        Item item = new Item();
        when(itemRepository.findById(anyLong())).thenReturn(item);
        when(itemConverter.convert(item)).thenReturn(new ItemDTO());

        ItemDTO result = itemService.getItemById(1L);

        assertNotNull(result);
        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowServiceExceptionWhenItemNotFoundById() {
        when(itemRepository.findById(anyLong())).thenReturn(null);

        assertThrows(ServiceException.class, () -> itemService.getItemById(1L));
    }

    @Test
    void shouldReturnAllItems() throws ServiceException {
        when(itemRepository.findAll()).thenReturn(Arrays.asList(new Item(), new Item()));
        when(itemConverter.convert(any(Item.class))).thenReturn(new ItemDTO());

        List<ItemDTO> result = itemService.getAllItems();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoItemsFound() throws ServiceException {
        when(itemRepository.findAll()).thenReturn(Collections.emptyList());

        List<ItemDTO> result = itemService.getAllItems();

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldDeleteItemById() {
        when(itemRepository.findById(anyLong())).thenReturn(new Item());

        boolean result = itemService.isDeleteById(1L);

        assertTrue(result);
        verify(orderRepository, times(1)).deleteByItemId(1L);
        verify(itemRepository, times(1)).removeById(1L);
    }

    @Test
    void shouldReturnFalseWhenItemToDeleteNotFound() {
        when(itemRepository.findById(anyLong())).thenReturn(null);

        boolean result = itemService.isDeleteById(1L);

        assertFalse(result);
    }

    @Test
    void shouldAddItemSuccessfully() throws ServiceException {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setTitle("Sample Item");
        Item item = new Item();

        when(itemConverter.convert(itemDTO)).thenReturn(item);
        when(itemConverter.convert(item)).thenReturn(new ItemDTO());

        ItemDTO result = itemService.addItem(itemDTO);

        assertNotNull(result);
        verify(itemRepository, times(1)).persist(any(Item.class));
    }

    @Test
    void shouldThrowServiceExceptionOnAddItemFailure() {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setTitle("Sample Item");

        when(itemConverter.convert(itemDTO)).thenThrow(RuntimeException.class);

        assertThrows(ServiceException.class, () -> itemService.addItem(itemDTO));
    }


    @Test
    void shouldCopyItemByIdSuccessfully() throws ServiceException {
        Item originalItem = new Item();
        originalItem.setId(1L);
        originalItem.setTitle("Original Title");

        when(itemRepository.findById(anyLong())).thenReturn(originalItem);

        itemService.copyItemById(1L);

        ArgumentCaptor<Item> itemArgumentCaptor = ArgumentCaptor.forClass(Item.class);
        verify(itemRepository).persist(itemArgumentCaptor.capture());
        Item copiedItem = itemArgumentCaptor.getValue();

        assertNotEquals(originalItem.getId(), copiedItem.getId());
        assertEquals("Original Title", copiedItem.getTitle());
        assertNotNull(copiedItem.getUuid());
    }

    @Test
    void shouldThrowServiceExceptionWhenOriginalItemNotFound() {
        when(itemRepository.findById(anyLong())).thenReturn(null);

        assertThrows(ServiceException.class, () -> itemService.copyItemById(1L));

        verify(itemRepository, never()).persist(any(Item.class));
    }

    @Test
    void shouldThrowServiceExceptionWhenErrorOccursFetchingAllItems() {
        when(itemRepository.findAll()).thenThrow(new RuntimeException("Unexpected error"));

        ServiceException thrown = assertThrows(ServiceException.class, () -> itemService.getAllItems());

        assertEquals("An error occurred while fetching all items.", thrown.getMessage());
    }

    @Test
    void shouldReturnFalseWhenExceptionOccursOnDeleteById() {
        Long itemId = 1L;
        when(itemRepository.findById(itemId)).thenReturn(new Item());
        doThrow(new RuntimeException("Database error")).when(orderRepository).deleteByItemId(itemId);

        boolean result = itemService.isDeleteById(itemId);

        assertFalse(result);
    }
}