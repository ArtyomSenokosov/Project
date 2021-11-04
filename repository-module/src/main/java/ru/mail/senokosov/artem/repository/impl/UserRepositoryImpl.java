package ru.mail.senokosov.artem.repository.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.UserRepository;
import ru.mail.senokosov.artem.repository.model.User;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Log4j2
@Repository
public class UserRepositoryImpl extends GenericRepositoryImpl<Long, User> implements UserRepository {

    @Override
    public User findUserByUsername(String email) {
        String stringQuery = "SELECT u FROM User as u WHERE u.email=:email";
        Query query = entityManager.createQuery(stringQuery);
        query.setParameter("email", email);
        try {
            query.getSingleResult();
        } catch (NoResultException e) {
            log.error(e.getMessage(), e);
            return null;
        }
        return (User) query.getSingleResult();
    }

    @Override
    public Long getCountUsers() {
        String hql = "SELECT COUNT(u.id) FROM User as u";
        Query query = entityManager.createQuery(hql);
        return (Long) query.getSingleResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> findAll(int startPosition, int maximumUsersOnPage) {
        String hql = "SELECT u FROM User as u ORDER BY u.email ASC";
        Query query = entityManager.createQuery(hql);
        query.setFirstResult(startPosition);
        query.setMaxResults(maximumUsersOnPage);
        return query.getResultList();
    }
}