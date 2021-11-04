package ru.mail.senokosov.artem.service;

import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.NewsDTO;
import ru.mail.senokosov.artem.service.model.PageDTO;

import java.util.List;

public interface NewsService {

    PageDTO getNewsByPage(Integer page);

    NewsDTO getNewsById(Long id) throws ServiceException;

    List<NewsDTO> getAllNews() throws ServiceException;

    boolean isDeleteById(Long id);

    NewsDTO addNews(NewsDTO newsDTO) throws ServiceException;

    void updateNews(NewsDTO newsDTO) throws ServiceException;
}