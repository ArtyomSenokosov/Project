package ru.mail.senokosov.artem.service;

import org.hibernate.service.spi.ServiceException;
import ru.mail.senokosov.artem.service.model.change.ChangeArticleDTO;
import ru.mail.senokosov.artem.service.model.PageDTO;
import ru.mail.senokosov.artem.service.model.add.AddArticleDTO;
import ru.mail.senokosov.artem.service.model.show.ShowArticleDTO;

import java.util.List;

public interface ArticleService {

    PageDTO getArticlesByPage(Integer page);

    ShowArticleDTO getArticleById(Long id) throws ServiceException, ru.mail.senokosov.artem.service.exception.ServiceException;

    List<ShowArticleDTO> getArticles();

    boolean isDeleteById(Long id) throws ServiceException;

    ShowArticleDTO add(AddArticleDTO addArticleDTO) throws ServiceException, ru.mail.senokosov.artem.service.exception.ServiceException;

    ShowArticleDTO changeParameterById(ChangeArticleDTO changeArticleDTO, Long id) throws ServiceException, ru.mail.senokosov.artem.service.exception.ServiceException;
}