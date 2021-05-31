package ru.mail.senokosov.artem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.service.spi.ServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.mail.senokosov.artem.repository.ArticleRepository;
import ru.mail.senokosov.artem.repository.PagingAndSortingRepository;
import ru.mail.senokosov.artem.repository.UserRepository;
import ru.mail.senokosov.artem.repository.model.Article;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.ArticleService;
import ru.mail.senokosov.artem.service.converter.ArticleConverter;
import ru.mail.senokosov.artem.service.model.add.AddArticleDTO;
import ru.mail.senokosov.artem.service.model.ArticleDTO;
import ru.mail.senokosov.artem.service.model.show.ShowArticleDTO;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleConverter articleConverter;
    private final UserRepository userRepository;
    private final ThreadLocal<PagingAndSortingRepository> pagingAndSortingRepository = new ThreadLocal<PagingAndSortingRepository>();

    @Override
    @Transactional
    public Page<ShowArticleDTO> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.pagingAndSortingRepository.get().findAll(pageable);
    }

    @Override
    @Transactional
    public List<ShowArticleDTO> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        return articles.stream()
                .map(articleConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ShowArticleDTO getArticleById(Long id) {
        Article article = articleRepository.findById(id);
        return articleConverter.convert(article);
    }

    @Override
    @Transactional
    public void persist(AddArticleDTO addArticleDTO) {
        Long userId = addArticleDTO.getUserId();
        Article article = articleConverter.convert(addArticleDTO);
        User user = userRepository.findById(userId);
        if (Objects.nonNull(user)) {
            article.setUser(user);
        }
        articleRepository.persist(article);
    }

    @Override
    @Transactional
    public boolean deleteArticleById(Long id) throws SecurityException {
        Article byId = articleRepository.findById(id);
        articleRepository.remove(byId);
        return true;
    }

    @Override
    @Transactional
    public ArticleDTO changeTitleById(ArticleDTO articleDTO) throws ServiceException {
        Article article = articleRepository.findById(articleDTO.getId());
        String title = articleDTO.getTitle();
        article.setTitle(title);
        articleRepository.merge(article);
        return articleConverter.convertToChange(article);
    }

    @Override
    @Transactional
    public ArticleDTO changeContentById(ArticleDTO articleDTO) throws ServiceException {
        Article article = articleRepository.findById(articleDTO.getId());
        String content = articleDTO.getFullContent();
        article.setFullContent(content);
        articleRepository.merge(article);
        return articleConverter.convertToChange(article);
    }
}
