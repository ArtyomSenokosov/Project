package ru.mail.senokosov.artem.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import ru.mail.senokosov.artem.repository.impl.ItemRepositoryImpl;
import ru.mail.senokosov.artem.repository.model.Item;
import ru.mail.senokosov.artem.service.exception.ServiceException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ItemLoaderServiceImplTest {

    @Mock
    private ItemRepositoryImpl itemRepository;
    @Mock
    private ResourceLoader resourceLoader;
    @Mock
    private InputStream inputStream;

    @InjectMocks
    private ItemLoaderServiceImpl itemLoaderService;

    private static final String JSON_FILE_PATH = "src/test/resources/items.json";

    @BeforeEach
    void setUp() {
        Resource mockResource = new ClassPathResource("test-items.json", this.getClass().getClassLoader());
        when(resourceLoader.getResource(JSON_FILE_PATH)).thenReturn(mockResource);
    }

    @Test
    void shouldLoadItemsFromJsonSuccessfully() throws ServiceException, IOException {
        List<Item> items = List.of(createItemWithUuid("00000000-0000-0000-0000-000000000001"),
                createItemWithUuid("00000000-0000-0000-0000-000000000002"));
        ObjectMapper objectMapper = new ObjectMapper();
        when(itemRepository.findByUuid(any(UUID.class))).thenReturn(null);
        when(inputStream.readAllBytes()).thenReturn(objectMapper.writeValueAsBytes(items));

        itemLoaderService.loadItemsFromJson(JSON_FILE_PATH);

        verify(itemRepository, times(items.size())).persist(any(Item.class));
    }

    @Test
    void shouldNotPersistDuplicateItems() throws ServiceException, IOException {
        Item duplicateItem = createItemWithUuid("00000000-0000-0000-0000-000000000001");
        Item uniqueItem = createItemWithUuid("00000000-0000-0000-0000-000000000002");
        List<Item> items = List.of(duplicateItem, uniqueItem, duplicateItem);
        ObjectMapper objectMapper = new ObjectMapper();

        when(itemRepository.findByUuid(duplicateItem.getUuid())).thenReturn(new Item());
        when(itemRepository.findByUuid(uniqueItem.getUuid())).thenReturn(null);
        when(inputStream.readAllBytes()).thenReturn(objectMapper.writeValueAsBytes(items));

        itemLoaderService.loadItemsFromJson(JSON_FILE_PATH);

        verify(itemRepository).persist(argThat(item -> item.getUuid().equals(uniqueItem.getUuid())));
    }

    @Test
    void shouldThrowServiceExceptionOnIOException() throws IOException {
        Resource mockResource = mock(Resource.class);
        when(resourceLoader.getResource(JSON_FILE_PATH)).thenReturn(mockResource);
        when(mockResource.getInputStream()).thenThrow(new IOException("Failed to read"));

        ServiceException thrown = assertThrows(ServiceException.class,
                () -> itemLoaderService.loadItemsFromJson(JSON_FILE_PATH),
                "Expected loadItemsFromJson to throw ServiceException, but it didn't");

        assertTrue(thrown.getMessage().contains("Error loading items from JSON file"));
    }

    @Test
    void shouldCloseInputStreamOnSuccess() throws ServiceException, IOException {
        String jsonContent = "[{\"uuid\":\"00000000-0000-0000-0000-000000000001\"}, {\"uuid\":\"00000000-0000-0000-0000-000000000002\"}]";
        byte[] jsonData = jsonContent.getBytes(StandardCharsets.UTF_8);

        InputStream mockInputStream = new ByteArrayInputStream(jsonData);
        Resource mockResource = mock(Resource.class);
        when(resourceLoader.getResource(JSON_FILE_PATH)).thenReturn(mockResource);
        when(mockResource.getInputStream()).thenReturn(mockInputStream);

        itemLoaderService.loadItemsFromJson(JSON_FILE_PATH);
    }

    @Test
    void shouldCloseInputStreamOnIOException() throws IOException {
        InputStream mockInputStream = mock(InputStream.class);
        Resource mockResource = mock(Resource.class);
        when(resourceLoader.getResource(JSON_FILE_PATH)).thenReturn(mockResource);
        when(mockResource.getInputStream()).thenReturn(mockInputStream);
        when(mockInputStream.readAllBytes()).thenThrow(new IOException("Failed to read"));

        assertThrows(ServiceException.class, () -> itemLoaderService.loadItemsFromJson(JSON_FILE_PATH));

        verify(mockInputStream, atLeastOnce()).close();
    }

    @Test
    void shouldCloseInputStreamOnJsonParseException() throws IOException {
        InputStream mockInputStream = mock(InputStream.class);
        when(mockInputStream.readAllBytes()).thenReturn("{malformedJson:true".getBytes());
        Resource mockResource = mock(Resource.class);
        when(resourceLoader.getResource(JSON_FILE_PATH)).thenReturn(mockResource);
        when(mockResource.getInputStream()).thenReturn(mockInputStream);

        assertThrows(ServiceException.class, () -> itemLoaderService.loadItemsFromJson(JSON_FILE_PATH),
                "Expected loadItemsFromJson to throw ServiceException due to malformed JSON, but it didn't.");

        verify(mockInputStream, atLeastOnce()).close();
    }

    private Item createItemWithUuid(String uuidString) {
        Item item = new Item();
        item.setUuid(UUID.fromString(uuidString));
        return item;
    }
}