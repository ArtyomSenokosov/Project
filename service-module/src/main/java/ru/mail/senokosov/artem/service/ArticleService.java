package ru.mail.senokosov.artem.service;

import org.hibernate.service.spi.ServiceException;
import org.springframework.data.domain.Page;
import ru.mail.senokosov.artem.service.model.add.AddArticleDTO;
import ru.mail.senokosov.artem.service.model.ArticleDTO;
import ru.mail.senokosov.artem.service.model.show.ShowArticleDTO;

import java.util.List;

public interface ArticleService {

    Page<ShowArticleDTO> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

    List<ShowArticleDTO> getAllArticles();

    ShowArticleDTO getArticleById(Long id);

    void persist(AddArticleDTO addArticleDTO);

    boolean deleteArticleById(Long id) throws SecurityException;

    ArticleDTO changeTitleById(ArticleDTO articleDTO) throws ServiceException;

    ArticleDTO changeContentById(ArticleDTO articleDTO) throws ServiceException;
}
