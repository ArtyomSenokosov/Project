package ru.mail.senokosov.artem.repository;

import ru.mail.senokosov.artem.repository.model.User;

public interface UserRepository extends GenericRepository<Long, User> {

    User getUserByEmail(String email);
}
