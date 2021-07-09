package ru.mail.senokosov.artem.service.converter.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import ru.mail.senokosov.artem.repository.model.Article;
import ru.mail.senokosov.artem.repository.model.Comment;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.converter.ArticleConverter;
import ru.mail.senokosov.artem.service.converter.CommentConverter;
import ru.mail.senokosov.artem.service.model.add.AddArticleDTO;
import ru.mail.senokosov.artem.service.model.show.ShowArticleDTO;
import ru.mail.senokosov.artem.service.model.show.ShowCommentDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.mail.senokosov.artem.service.constant.ArticleConstant.MAXIMUM_CHARS_FOR_SHORT_CONTENT_FIELD;
import static ru.mail.senokosov.artem.service.util.ServiceUtil.getFormatDateTime;

@Component
@Log4j2
@RequiredArgsConstructor
public class ArticleConverterImpl implements ArticleConverter {

    private final CommentConverter commentConverter;

    @Override
    public ShowArticleDTO convert(Article article) {
        ShowArticleDTO showArticleDTO = new ShowArticleDTO();
        Long id = article.getId();
        showArticleDTO.setId(id);
        LocalDateTime localDateTime = article.getLocalDateTime();
        if (Objects.nonNull(localDateTime)) {
            String formatDateTime = getFormatDateTime(localDateTime);
            showArticleDTO.setDate(formatDateTime);
        }
        String title = article.getTitle();
        if (Objects.nonNull(title)) {
            showArticleDTO.setTitle(title);
        }
        User user = article.getUser();
        if (Objects.nonNull(user)) {
            String firstName = user.getFirstName();
            showArticleDTO.setFirstName(firstName);
            String lastName = user.getLastName();
            showArticleDTO.setLastName(lastName);
        }
        String fullContent = article.getFullContent();
        if (Objects.nonNull(fullContent)) {
            showArticleDTO.setFullContent(fullContent);
            addShortContent(showArticleDTO, fullContent);
        }
        Set<Comment> comments = article.getComments();
        if (!comments.isEmpty()) {
            List<ShowCommentDTO> commentDTOs = comments.stream()
                    .map(commentConverter::convert)
                    .collect(Collectors.toList());
            showArticleDTO.getComments().addAll(commentDTOs);
        }
        return showArticleDTO;
    }

    private void addShortContent(ShowArticleDTO showArticleDTO, String fullContent) {
        if (fullContent.length() > MAXIMUM_CHARS_FOR_SHORT_CONTENT_FIELD) {
            String shortContent = fullContent.substring(0, MAXIMUM_CHARS_FOR_SHORT_CONTENT_FIELD);
            showArticleDTO.setShortContent(shortContent);
        } else {
            showArticleDTO.setShortContent(fullContent);
        }
    }

    @Override
    public Article convert(AddArticleDTO addArticleDTO) {
        Article article = new Article();
        String title = addArticleDTO.getTitle();
        article.setTitle(title);
        String content = addArticleDTO.getContent();
        article.setFullContent(content);
        return article;
    }
}