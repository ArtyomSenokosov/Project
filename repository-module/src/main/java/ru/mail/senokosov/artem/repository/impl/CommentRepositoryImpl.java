package ru.mail.senokosov.artem.repository.impl;

import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.CommentRepository;
import ru.mail.senokosov.artem.repository.model.Comment;

import javax.persistence.Query;
import java.util.List;

@Repository
public class CommentRepositoryImpl extends GenericRepositoryImpl<Long, Comment> implements CommentRepository {

    @Override
    @SuppressWarnings("unchecked")
    public List<Comment> findCommentByArticleId(Long id) {
        String hql = "SELECT c FROM Comment as c WHERE c.article.id=:articleId ORDER BY c.localDateTime DESC";
        Query query = entityManager.createQuery(hql);
        query.setParameter("articleId", id);
        return query.getResultList();
    }
}