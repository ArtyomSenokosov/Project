package ru.mail.senokosov.artem.service;

import ru.mail.senokosov.artem.service.model.add.AddCommentDTO;
import ru.mail.senokosov.artem.service.model.show.ShowCommentDTO;

import java.util.List;

public interface CommentService {

    List<ShowCommentDTO> getAllComments();

    void persist(AddCommentDTO addCommentDTO, String userName, Long articleId);
}
