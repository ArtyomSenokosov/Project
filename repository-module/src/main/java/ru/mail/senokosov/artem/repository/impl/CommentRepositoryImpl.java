package ru.mail.senokosov.artem.repository.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.CommentRepository;
import ru.mail.senokosov.artem.repository.model.Comment;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;

import static ru.mail.senokosov.artem.repository.constant.RepositoryConstants.ARTICLE_PARAMETER;

@Repository
@Log4j2
public class CommentRepositoryImpl extends GenericRepositoryImpl<Long, Comment> implements CommentRepository {

    @Override
    @SuppressWarnings("unchecked")
    public List<Comment> findCommentByArticleId(Long id) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Comment> query = criteriaBuilder.createQuery(Comment.class);
            Root<Comment> commentRoot = query.from(Comment.class);
            query.select(commentRoot);
            ParameterExpression<Long> parameter = criteriaBuilder.parameter(Long.class);
            query.where(criteriaBuilder.equal(commentRoot.get(ARTICLE_PARAMETER), parameter));
            TypedQuery<Comment> typedQuery = entityManager.createQuery(query);
            typedQuery.setParameter(parameter, id);
            return typedQuery.getResultList();
        } catch (NoResultException e) {
            log.info("Comment does not exist");
            return null;
        }
    }
}
