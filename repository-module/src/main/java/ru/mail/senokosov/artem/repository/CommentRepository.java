package ru.mail.senokosov.artem.repository;

import ru.mail.senokosov.artem.repository.model.Comment;

public interface CommentRepository extends GenericRepository<Long, Comment> {

    void deleteByNewsId(Long newsId);
}