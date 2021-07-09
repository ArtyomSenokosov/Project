package ru.mail.senokosov.artem.repository;

import ru.mail.senokosov.artem.repository.model.User;

import java.util.List;

public interface UserRepository extends GenericRepository<Long, User> {

    User findUserByUsername(String email);

    Long getCountUsers();

    List<User> findAll(int startPosition, int maximumUsersOnPage);
}