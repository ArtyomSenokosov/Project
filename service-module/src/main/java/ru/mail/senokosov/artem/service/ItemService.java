package ru.mail.senokosov.artem.service;

import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.PageDTO;
import ru.mail.senokosov.artem.service.model.add.AddItemDTO;
import ru.mail.senokosov.artem.service.model.show.ShowItemDTO;

import java.util.List;
import java.util.UUID;

public interface ItemService {

    PageDTO getItemsByPage(int page);

    List<ShowItemDTO> getItems();

    ShowItemDTO getItemById(Long id) throws ServiceException;

    ShowItemDTO persist(AddItemDTO addItemDTO);

    boolean isDeleteById(Long id);

    ShowItemDTO getItemByUuid(UUID uuid) throws ServiceException;

    boolean isDeleteByUuid(UUID uuid) throws ServiceException;

    ShowItemDTO CopyItemByUuid(UUID uuid) throws ServiceException;
}