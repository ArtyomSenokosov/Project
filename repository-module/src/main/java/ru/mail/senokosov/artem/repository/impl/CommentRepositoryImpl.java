package ru.mail.senokosov.artem.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.CommentRepository;
import ru.mail.senokosov.artem.repository.model.Comment;

import javax.persistence.Query;
import javax.transaction.Transactional;

@Slf4j
@Repository
public class CommentRepositoryImpl extends GenericRepositoryImpl<Long, Comment> implements CommentRepository {

    @Override
    @Transactional
    public void deleteByNewsId(Long newsId) {
        log.debug("Deleting comments related to newsId: {}", newsId);
        String hql = "DELETE FROM Comment c WHERE c.news.id = :newsId";
        Query query = entityManager.createQuery(hql);
        query.setParameter("newsId", newsId);
        int deletedCount = query.executeUpdate();
        log.info("Deleted {} comments related to newsId: {}", deletedCount, newsId);
    }
}