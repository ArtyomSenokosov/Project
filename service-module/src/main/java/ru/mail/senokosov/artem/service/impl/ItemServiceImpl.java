package ru.mail.senokosov.artem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import ru.mail.senokosov.artem.repository.ItemRepository;
import ru.mail.senokosov.artem.repository.OrderRepository;
import ru.mail.senokosov.artem.repository.model.Item;
import ru.mail.senokosov.artem.service.ItemService;
import ru.mail.senokosov.artem.service.converter.ItemConverter;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.ItemDTO;
import ru.mail.senokosov.artem.service.model.PageDTO;
import ru.mail.senokosov.artem.service.model.PaginationResult;
import ru.mail.senokosov.artem.service.util.PaginationUtil;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static ru.mail.senokosov.artem.service.constant.ItemConstant.MAXIMUM_ITEMS_ON_PAGE;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final ItemConverter itemConverter;

    @Override
    @Transactional
    public PageDTO getItemsByPage(Integer page) {
        log.debug("Requesting page number {} for items.", page);

        Long countItems = itemRepository.getCount();

        PaginationResult pagination = PaginationUtil.calculatePagination(countItems, MAXIMUM_ITEMS_ON_PAGE, page);

        List<Item> items = itemRepository.findAll(pagination.getStartPosition(), MAXIMUM_ITEMS_ON_PAGE);
        log.debug("Retrieved {} items for page {}.", items.size(), pagination.getCurrentPage());

        List<ItemDTO> itemDTOs = items.stream()
                .map(itemConverter::convert)
                .collect(Collectors.toList());

        PageDTO pageDTO = new PageDTO();
        pageDTO.setTotalPages(pagination.getTotalPages());
        pageDTO.setCurrentPage((long) pagination.getCurrentPage());
        pageDTO.setItems(itemDTOs);
        pageDTO.setStartPosition(pagination.getStartPosition());

        log.info("Page {} of items served with {} items.", pagination.getCurrentPage(), itemDTOs.size());
        return pageDTO;
    }

    @Override
    @Transactional
    public ItemDTO getItemById(Long id) throws ServiceException {
        log.debug("Attempting to find item by id: {}", id);

        Item item = itemRepository.findById(id);
        if (Objects.nonNull(item)) {
            log.info("Item with id: {} found and being converted.", id);
            return itemConverter.convert(item);
        } else {
            log.warn("Item with id: {} was not found.", id);
            throw new ServiceException(String.format("Item with id: %s was not found", id));
        }
    }

    @Override
    @Transactional
    public List<ItemDTO> getAllItems() throws ServiceException {
        log.debug("Requesting all items.");

        try {
            List<Item> items = itemRepository.findAll();
            if (items.isEmpty()) {
                log.info("No items were found.");
                return Collections.emptyList();
            }

            List<ItemDTO> itemDTOs = items.stream()
                    .map(itemConverter::convert)
                    .collect(Collectors.toList());

            log.info("Retrieved {} items.", itemDTOs.size());
            return itemDTOs;
        } catch (Exception exception) {
            log.error("An error occurred while fetching all items: {}", exception.getMessage());
            throw new ServiceException("An error occurred while fetching all items.");
        }
    }

    @Override
    @Transactional
    public boolean isDeleteById(Long id) {
        try {
            if (Objects.nonNull(itemRepository.findById(id))) {
                orderRepository.deleteByItemId(id);
                log.debug("All orders related to item with id {} have been deleted.", id);

                itemRepository.removeById(id);
                log.info("Item with id {} has been successfully deleted.", id);
                return true;
            } else {
                log.warn("Item with id {} not found. Deletion cannot be performed.", id);
                return false;
            }
        } catch (Exception exception) {
            log.error("Error occurred during the deletion of item with id {}: {}", id, exception.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public ItemDTO addItem(ItemDTO itemDTO) throws ServiceException {
        log.debug("Starting process to add a new item with title: {}", itemDTO.getTitle());
        try {
            Item item = itemConverter.convert(itemDTO);
            UUID uuid = UUID.randomUUID();
            item.setUuid(uuid);
            itemRepository.persist(item);

            ItemDTO createdItemDTO = itemConverter.convert(item);
            log.info("Item titled '{}' has been successfully added with UUID: {}", itemDTO.getTitle(), createdItemDTO.getUuid());
            return createdItemDTO;
        } catch (Exception exception) {
            log.error("Failed to add item titled '{}': {}", itemDTO.getTitle(), exception.getMessage());
            throw new ServiceException(String.format("Failed to add item titled '%s'", itemDTO.getTitle()));
        }
    }

    @Override
    @Transactional
    public void copyItemById(Long id) throws ServiceException {
        log.debug("Attempting to copy item with id: {}", id);

        Item originalItem = itemRepository.findById(id);
        if (Objects.isNull(originalItem)) {
            log.warn("Item not found for id: {}", id);
            throw new ServiceException("Item not found for id: " + id);
        }

        Item copiedItem = new Item();
        BeanUtils.copyProperties(originalItem, copiedItem, "id", "unique_number");
        copiedItem.setUuid(UUID.randomUUID());

        itemRepository.persist(copiedItem);
        log.info("Successfully copied item. Original item id: {}, new item id: {}", id, copiedItem.getId());
    }
}