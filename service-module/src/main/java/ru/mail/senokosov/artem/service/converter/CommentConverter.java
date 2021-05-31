package ru.mail.senokosov.artem.service.converter;

import ru.mail.senokosov.artem.repository.model.Article;
import ru.mail.senokosov.artem.repository.model.Comment;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.model.add.AddCommentDTO;
import ru.mail.senokosov.artem.service.model.show.ShowCommentDTO;

public interface CommentConverter {

    ShowCommentDTO convert(Comment comment);

    Comment convert(AddCommentDTO addCommentDTO, User user, Article article);
}
