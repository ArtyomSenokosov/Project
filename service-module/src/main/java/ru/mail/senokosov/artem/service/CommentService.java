package ru.mail.senokosov.artem.service;

import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.add.AddCommentDTO;
import ru.mail.senokosov.artem.service.model.show.ShowCommentDTO;

public interface CommentService {

    ShowCommentDTO persist(AddCommentDTO addCommentDTO, Long articleId) throws ServiceException;

    boolean isDeleteById(Long id);
}