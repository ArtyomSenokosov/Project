package ru.mail.senokosov.artem.repository;

import ru.mail.senokosov.artem.repository.model.Comment;

import java.util.List;

public interface CommentRepository extends GenericRepository<Long, Comment> {

    List<Comment> findCommentByArticleId(Long id);
}
