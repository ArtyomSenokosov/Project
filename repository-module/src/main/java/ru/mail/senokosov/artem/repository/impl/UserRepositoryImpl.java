package ru.mail.senokosov.artem.repository.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.UserRepository;
import ru.mail.senokosov.artem.repository.model.User;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;

import static ru.mail.senokosov.artem.repository.constant.RepositoryConstants.*;

@Repository
@Log4j2
public class UserRepositoryImpl extends GenericRepositoryImpl<Long, User> implements UserRepository {

    @Override
    public User findUserByUsername(String email) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> userQuery = criteriaBuilder.createQuery(User.class);
            Root<User> userRoot = userQuery.from(User.class);
            userQuery.select(userRoot);
            ParameterExpression<Long> parameter = criteriaBuilder.parameter(Long.class);
            userQuery.where(criteriaBuilder.equal(userRoot.get(ID_PARAMETER), parameter));
            TypedQuery<User> typedQuery = entityManager.createQuery(userQuery);
            typedQuery.setParameter(String.valueOf(parameter), email);
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Long getCountUsers() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(User.class)));
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> findAll(int startPosition, int maximumUsersOnPage) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> userQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = userQuery.from(User.class);
        CriteriaQuery<User> select = userQuery.select(userRoot)
                .orderBy(criteriaBuilder.asc(userRoot.get(EMAIL_PARAMETER)));
        TypedQuery<User> typedQuery = entityManager.createQuery(select);
        typedQuery.setFirstResult(startPosition);
        typedQuery.setMaxResults(maximumUsersOnPage);
        return typedQuery.getResultList();
    }
}