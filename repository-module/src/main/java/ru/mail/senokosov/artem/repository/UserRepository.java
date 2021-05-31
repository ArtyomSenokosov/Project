package ru.mail.senokosov.artem.repository;

import ru.mail.senokosov.artem.repository.model.User;

import java.util.List;

public interface UserRepository extends GenericRepository<Long, User> {

    User findUserByEmail(String email);

    List<User> findAll(int pageNumber, int pageSize);

    Long getCountUsers();
}
