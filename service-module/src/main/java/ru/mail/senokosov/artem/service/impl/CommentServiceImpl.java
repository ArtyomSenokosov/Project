package ru.mail.senokosov.artem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.mail.senokosov.artem.repository.ArticleRepository;
import ru.mail.senokosov.artem.repository.CommentRepository;
import ru.mail.senokosov.artem.repository.UserRepository;
import ru.mail.senokosov.artem.repository.model.Article;
import ru.mail.senokosov.artem.repository.model.Comment;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.CommentService;
import ru.mail.senokosov.artem.service.converter.CommentConverter;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.add.AddCommentDTO;
import ru.mail.senokosov.artem.service.model.show.ShowCommentDTO;

import javax.transaction.Transactional;
import java.util.Objects;

import static ru.mail.senokosov.artem.service.util.SecurityUtil.getAuthentication;

@Log4j2
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentConverter commentConverter;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    @Override
    @Transactional
    public ShowCommentDTO persist(AddCommentDTO addCommentDTO, Long articleId) throws ServiceException {
        Authentication authentication = getAuthentication();
        String userName = authentication.getName();
        User user = userRepository.findUserByUsername(userName);
        if (Objects.nonNull(user)) {
            Comment comment = commentConverter.convert(addCommentDTO);
            comment.setUser(user);
            Article article = articleRepository.findById(articleId);
            if (Objects.nonNull(article)) {
                comment.setArticle(article);
                commentRepository.persist(comment);
                return commentConverter.convert(comment);
            } else {
                throw new ServiceException(String.format("Article with id: %s was not found", articleId));
            }
        } else {
            throw new ServiceException(String.format("User with username: %s was not found", userName));
        }
    }

    @Override
    @Transactional
    public boolean isDeleteById(Long id) {
        commentRepository.removeById(id);
        return true;
    }
}