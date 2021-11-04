package ru.mail.senokosov.artem.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.UserRepository;
import ru.mail.senokosov.artem.repository.model.User;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Slf4j
@Repository
public class UserRepositoryImpl extends GenericRepositoryImpl<Long, User> implements UserRepository {

    @Override
    public User findUserByEmail(String email) {
        log.debug("Attempting to find user by email: {}", email);
        String stringQuery = "SELECT u FROM User as u WHERE u.email=:email";
        Query query = entityManager.createQuery(stringQuery);
        query.setParameter("email", email);
        User user = null;
        try {
            user = (User) query.getSingleResult();
            log.debug("User found: {}", user);
        } catch (NoResultException exception) {
            log.info("No user found with email: {}", email);
        } catch (Exception exception) {
            log.error("An error occurred while finding user by email: {}", email, exception);
            throw exception;
        }
        return user;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> findAll(int startPosition, int maximumUsersOnPage) {
        log.debug("Querying all users with startPosition: {}, maximumUsersOnPage: {}",
                startPosition, maximumUsersOnPage);
        String hql = "SELECT u FROM User as u ORDER BY u.email ASC";
        Query query = entityManager.createQuery(hql);
        query.setFirstResult(startPosition);
        query.setMaxResults(maximumUsersOnPage);
        List<User> users = query.getResultList();
        log.debug("Found {} users", users.size());
        return users;
    }
}