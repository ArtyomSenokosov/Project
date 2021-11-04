package ru.mail.senokosov.artem.service;

import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.ItemDTO;
import ru.mail.senokosov.artem.service.model.PageDTO;

import java.util.List;

public interface ItemService {

    PageDTO getItemsByPage(Integer page);

    ItemDTO getItemById(Long id) throws ServiceException;

    List<ItemDTO> getAllItems() throws ServiceException;

    boolean isDeleteById(Long id);

    ItemDTO addItem(ItemDTO itemDTO) throws ServiceException;

    void copyItemById(Long id) throws ServiceException;
}