package ru.mail.senokosov.artem.service;

import ru.mail.senokosov.artem.service.exception.ServiceException;

public interface ItemLoaderService {

    void loadItemsFromJson(String jsonFilePath) throws ServiceException;
}
