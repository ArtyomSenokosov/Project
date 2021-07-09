package ru.mail.senokosov.artem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.mail.senokosov.artem.repository.ItemInfoRepository;
import ru.mail.senokosov.artem.repository.ItemRepository;
import ru.mail.senokosov.artem.repository.model.Item;
import ru.mail.senokosov.artem.repository.model.ItemInfo;
import ru.mail.senokosov.artem.service.ItemService;
import ru.mail.senokosov.artem.service.converter.ItemConverter;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.PageDTO;
import ru.mail.senokosov.artem.service.model.add.AddItemDTO;
import ru.mail.senokosov.artem.service.model.show.ShowItemDTO;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static ru.mail.senokosov.artem.service.constant.ItemConstant.MAXIMUM_ITEMS_ON_PAGE;
import static ru.mail.senokosov.artem.service.util.ServiceUtil.getPageDTO;

@Service
@Log4j2
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemConverter itemConverter;
    private final ItemInfoRepository itemInfoRepository;

    @Override
    @Transactional
    public PageDTO getItemsByPage(int page) {
        Long countItems = itemRepository.getCountItems();
        PageDTO pageDTO = getPageDTO(page, countItems, MAXIMUM_ITEMS_ON_PAGE);
        List<Item> items = itemRepository.findAll(pageDTO.getStartPosition(), MAXIMUM_ITEMS_ON_PAGE);
        pageDTO.getItems().addAll(items.stream()
                .map(itemConverter::convert)
                .collect(Collectors.toList()));
        return pageDTO;
    }

    @Override
    @Transactional
    public List<ShowItemDTO> getItems() {
        List<Item> items = itemRepository.findAll();
        return items.stream()
                .map(itemConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ShowItemDTO getItemById(Long id) throws ServiceException {
        Item item = itemRepository.findById(id);
        if (Objects.nonNull(item)) {
            return itemConverter.convert(item);
        } else {
            throw new ServiceException(String.format("Item with id: %s was not found", id));
        }
    }

    @Override
    @Transactional
    public ShowItemDTO persist(AddItemDTO addItemDTO) {
        Item item = itemConverter.convert(addItemDTO);
        UUID uuid = UUID.randomUUID();
        item.setUuid(uuid);
        itemRepository.persist(item);
        return itemConverter.convert(item);
    }

    @Override
    @Transactional
    public boolean isDeleteById(Long id) {
        itemRepository.removeById(id);
        return true;
    }

    @Override
    @Transactional
    public ShowItemDTO getItemByUuid(UUID uuid) throws ServiceException {
        Item item = itemRepository.findByUuid(uuid);
        if (Objects.nonNull(item)) {
            return itemConverter.convert(item);
        } else {
            throw new ServiceException(String.format("Item with uuid: %s was not found", uuid));
        }
    }

    @Override
    @Transactional
    public boolean isDeleteByUuid(UUID uuid) throws ServiceException {
        Item item = itemRepository.findByUuid(uuid);
        if (Objects.nonNull(item)) {
            itemRepository.removeById(item.getId());
            return true;
        } else {
            throw new ServiceException(String.format("Item with uuid: %s was not found", uuid));
        }
    }

    @Override
    @Transactional
    public ShowItemDTO CopyItemByUuid(UUID uuid) throws ServiceException {
        Item item = itemRepository.findByUuid(uuid);
        if (Objects.nonNull(item)) {
            Item cloneItem = copyFieldsByItem(item);
            itemRepository.merge(cloneItem);
            ShowItemDTO showItemDTO = itemConverter.convert(cloneItem);
            return showItemDTO;
        } else {
            throw new ServiceException(String.format("Item with uuid: %s was not found", uuid));
        }
    }

    private Item copyFieldsByItem(Item item) {
        itemRepository.detach(item);
        item.setId(null);
        UUID uuid = UUID.randomUUID();
        item.setUuid(uuid);
        String title = item.getTitle();
        item.setTitle(String.format("copy of %s", title));

        ItemInfo itemInfo = item.getItemInfo();
        if (Objects.nonNull(itemInfo)) {
            itemInfoRepository.detach(itemInfo);
            itemInfo.setId(null);
        }
        return item;
    }
}
