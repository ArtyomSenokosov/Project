package ru.mail.senokosov.artem.service.converter;

import ru.mail.senokosov.artem.repository.model.Comment;
import ru.mail.senokosov.artem.service.model.CommentDTO;

public interface CommentConverter {

    CommentDTO convert(Comment comment);

    Comment convert(CommentDTO commentDTO);
}