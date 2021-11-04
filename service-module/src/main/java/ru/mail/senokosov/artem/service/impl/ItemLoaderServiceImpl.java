package ru.mail.senokosov.artem.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import ru.mail.senokosov.artem.repository.impl.ItemRepositoryImpl;
import ru.mail.senokosov.artem.repository.model.Item;
import ru.mail.senokosov.artem.service.ItemLoaderService;
import ru.mail.senokosov.artem.service.exception.ServiceException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemLoaderServiceImpl implements ItemLoaderService {

    private final ItemRepositoryImpl itemRepository;
    private final ResourceLoader resourceLoader;

    @Override
    @Transactional
    public void loadItemsFromJson(String jsonFilePath) throws ServiceException {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = null;
        try {
            inputStream = resourceLoader.getResource(jsonFilePath).getInputStream();
            List<Item> items = objectMapper.readValue(inputStream, new TypeReference<>() {});
            for (Item item : items) {
                if (itemRepository.findByUuid(item.getUuid()) == null) {
                    itemRepository.persist(item);
                    log.info("Persisted item with UUID: {}", item.getUuid());
                } else {
                    log.info("Item with UUID {} already exists and was not persisted.", item.getUuid());
                }
            }
        } catch (IOException exception) {
            log.error("Error loading items from JSON file: {}. Error: {}",
                    jsonFilePath, exception.getMessage(), exception);
            throw new ServiceException("Error loading items from JSON file: "
                    + jsonFilePath + ". " + exception.getMessage());
        } finally {
            if (Objects.nonNull(inputStream)) {
                try {
                    inputStream.close();
                } catch (IOException exception) {
                    log.error("Failed to close input stream for JSON file: {}. Error: {}",
                            jsonFilePath, exception.getMessage(), exception);
                }
            }
        }
    }
}