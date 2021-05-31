package ru.mail.senokosov.artem.repository.impl;

import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.RoleRepository;
import ru.mail.senokosov.artem.repository.model.Role;

@Repository
public class RoleRepositoryImpl extends GenericRepositoryImpl<Long, Role> implements RoleRepository {
}
