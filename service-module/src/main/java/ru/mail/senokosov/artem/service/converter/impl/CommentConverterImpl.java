package ru.mail.senokosov.artem.service.converter.impl;

import org.springframework.stereotype.Component;
import ru.mail.senokosov.artem.repository.model.Article;
import ru.mail.senokosov.artem.repository.model.Comment;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.converter.CommentConverter;
import ru.mail.senokosov.artem.service.model.add.AddCommentDTO;
import ru.mail.senokosov.artem.service.model.show.ShowCommentDTO;

import java.time.LocalDateTime;
import java.util.Objects;

import static ru.mail.senokosov.artem.service.util.ServiceUtil.getFormatDateTime;

@Component
public class CommentConverterImpl implements CommentConverter {

    @Override
    public ShowCommentDTO convert(Comment comment) {
        ShowCommentDTO showCommentDTO = new ShowCommentDTO();
        Long id = comment.getId();
        showCommentDTO.setId(id);
        LocalDateTime localDateTime = comment.getLocalDateTime();
        if (Objects.nonNull(localDateTime)) {
            String formatDateTime = getFormatDateTime(localDateTime);
            showCommentDTO.setDate(formatDateTime);
        }
        String fullContent = comment.getFullContent();
        showCommentDTO.setFullContent(fullContent);
        User user = comment.getUser();
        if (Objects.nonNull(user)) {
            String firstName = user.getFirstname();
            showCommentDTO.setFirstName(firstName);
            String lastName = user.getSecondname();
            showCommentDTO.setLastName(lastName);
        }
        return showCommentDTO;
    }

    @Override
    public Comment convert(AddCommentDTO addCommentDTO, User user, Article article) {
        Comment comment = new Comment();
        comment.setArticle(article);
        comment.setUser(user);
        LocalDateTime localDateTime = LocalDateTime.now();
        comment.setLocalDateTime(localDateTime);
        String fullContent = addCommentDTO.getFullContent();
        comment.setFullContent(fullContent);
        return comment;
    }
}
