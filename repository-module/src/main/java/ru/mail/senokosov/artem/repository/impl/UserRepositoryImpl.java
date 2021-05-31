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

import static ru.mail.senokosov.artem.repository.constant.RepositoryConstants.EMAIL_PARAMETER;

@Repository
@Log4j2
public class UserRepositoryImpl extends GenericRepositoryImpl<Long, User> implements UserRepository {

    @Override
    public User findUserByEmail(String email) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
            Root<User> userRoot = query.from(User.class);
            query.select(userRoot);
            ParameterExpression<String> parameter = criteriaBuilder.parameter(String.class);
            query.where(criteriaBuilder.equal(userRoot.get(EMAIL_PARAMETER), parameter));
            TypedQuery<User> typedQuery = entityManager.createQuery(query);
            typedQuery.setParameter(parameter, email);
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            log.info("User with username " + email + " does not exist");
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> findAll(int pageNumber, int pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(User.class)));
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        CriteriaQuery<User> userQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = userQuery.from(User.class);
        CriteriaQuery<User> select = userQuery.select(userRoot)
                .orderBy(criteriaBuilder.asc(userRoot.get(EMAIL_PARAMETER)));
        TypedQuery<User> typedQuery = entityManager.createQuery(select);
        if (pageSize < count.intValue()) {
            typedQuery.setFirstResult((pageNumber) = pageSize - pageSize);
            typedQuery.setMaxResults(pageSize);
            return typedQuery.getResultList();
        }
        return typedQuery.getResultList();
    }

    @Override
    public Long getCountUsers() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(User.class)));
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
