package ru.mail.senokosov.artem.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.mail.senokosov.artem.repository.ArticleRepository;
import ru.mail.senokosov.artem.repository.UserRepository;
import ru.mail.senokosov.artem.repository.model.Article;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.converter.ArticleConverter;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.change.ChangeArticleDTO;
import ru.mail.senokosov.artem.service.model.PageDTO;
import ru.mail.senokosov.artem.service.model.add.AddArticleDTO;
import ru.mail.senokosov.artem.service.model.show.ShowArticleDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ArticleServiceImplTest {

    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private ArticleConverter articleConverter;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ArticleServiceImpl articleService;

    @Test
    void shouldGetArticlesByNumberPage() {
        int startPosition = 0;
        int maximumItemsOnPage = 10;
        List<Article> articles = new ArrayList<>();
        when(articleRepository.findAll(startPosition, maximumItemsOnPage)).thenReturn(articles);
        List<ShowArticleDTO> articleDTOS = articles.stream()
                .map(articleConverter::convert)
                .collect(Collectors.toList());
        PageDTO pageDTO = new PageDTO();
        pageDTO.getArticles().addAll(articleDTOS);

        PageDTO itemsByPage = articleService.getArticlesByPage(1);

        assertEquals(pageDTO.getItems(), itemsByPage.getItems());
    }

    @Test
    void shouldGetAllArticles() {
        List<Article> articlesWithMock = new ArrayList<>();
        when(articleRepository.findAll()).thenReturn(articlesWithMock);
        List<ShowArticleDTO> articlesDTOSWithMock = articlesWithMock.stream()
                .map(articleConverter::convert)
                .collect(Collectors.toList());

        List<Article> articles = articleRepository.findAll();
        List<ShowArticleDTO> articleDTOS = articles.stream()
                .map(articleConverter::convert)
                .collect(Collectors.toList());

        assertEquals(articlesDTOSWithMock, articleDTOS);
    }

    @Test
    void shouldFindArticleByIdAndReturnExceptionIfArticleNotFound() {
        Long id = 1L;
        assertThrows(ServiceException.class, () -> articleService.getArticleById(id));
    }

    @Test
    void shouldFindArticleByIdAndReturnNotNullIfArticleWasFound() {
        Long id = 1L;
        Article article = new Article();
        article.setId(id);
        when(articleRepository.findById(id)).thenReturn(article);
        ShowArticleDTO articleDTO = new ShowArticleDTO();
        assertNotNull(articleDTO);
    }

    @Test
    void shouldGetArticleById() throws ServiceException {
        Long id = 1L;
        Article article = new Article();
        article.setId(id);
        when(articleRepository.findById(id)).thenReturn(article);
        ShowArticleDTO articleDTO = new ShowArticleDTO();
        when(articleConverter.convert(article)).thenReturn(articleDTO);

        ShowArticleDTO articleById = articleService.getArticleById(id);

        assertEquals(articleDTO, articleById);
    }

    @Test
    void shouldDeleteArticleById() {
        Long id = 1L;
        boolean isDeleteArticle = articleService.isDeleteById(id);

        assertTrue(isDeleteArticle);
    }

    @Test
    void shouldAddArticleAndReturnRightAddDateIfAddedSuccessfully() throws ServiceException {
        User user = new User();
        user.setId(1L);
        user.setLastName("Test last name");
        Authentication authentication = getAuthenticationWithUserName();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userRepository.findUserByUsername(authentication.getName())).thenReturn(user);
        AddArticleDTO addArticleDTO = new AddArticleDTO();
        addArticleDTO.setId(1L);
        addArticleDTO.setTitle("test title");
        addArticleDTO.setContent("test content");
        addArticleDTO.setSellerId(user.getId());
        Article article = new Article();
        when(articleConverter.convert(addArticleDTO)).thenReturn(article);
        ShowArticleDTO showArticleDTO = new ShowArticleDTO();
        when(articleConverter.convert(article)).thenReturn(showArticleDTO);

        ShowArticleDTO showArticle = articleService.add(addArticleDTO);

        assertEquals(showArticle.getDate(), showArticleDTO.getDate());
    }


    @Test
    void shouldAddArticleAndReturnExceptionIfUserWasNotAuthentication() {
        AddArticleDTO addArticleDTO = new AddArticleDTO();
        assertThrows(ServiceException.class, () -> articleService.add(addArticleDTO));
    }

    @Test
    void shouldAddArticleAndReturnExceptionIfUserWasNotFoundWithUserName() {
        Authentication authentication = getAuthenticationWithUserName();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userRepository.findUserByUsername(authentication.getName())).thenReturn(null);
        AddArticleDTO addArticleDTO = new AddArticleDTO();
        Article article = new Article();
        when(articleConverter.convert(addArticleDTO)).thenReturn(article);

        assertThrows(ServiceException.class, () -> articleService.add(addArticleDTO));
    }

    @Test
    void shouldChangeParameterByIdAndReturnExceptionIfArticleWithIdNotFound() {
        Long id = 1L;
        assertThrows(ServiceException.class, () -> articleService.getArticleById(id));
    }

    @Test
    void shouldChangeParameterByIdAndReturnNotNullObject() throws ServiceException {
        Long id = 1L;
        Article article = new Article();
        when(articleRepository.findById(id)).thenReturn(article);
        ChangeArticleDTO changeArticleDTO = new ChangeArticleDTO();
        ShowArticleDTO showArticleDTO = new ShowArticleDTO();
        when(articleService.changeParameterById(changeArticleDTO, id)).thenReturn(showArticleDTO);

        assertNotNull(showArticleDTO);
    }

    private Authentication getAuthenticationWithUserName() {
        return new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return "test@gmail.com";
            }
        };
    }
}