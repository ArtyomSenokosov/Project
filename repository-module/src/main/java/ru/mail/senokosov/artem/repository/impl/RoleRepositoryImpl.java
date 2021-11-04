package ru.mail.senokosov.artem.repository.impl;

import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.RoleRepository;
import ru.mail.senokosov.artem.repository.model.Role;

import javax.persistence.Query;

@Repository
public class RoleRepositoryImpl extends GenericRepositoryImpl<Long, Role> implements RoleRepository {

    @Override
    public Role findByRoleName(String roleName) {
        String stringQuery = "SELECT r FROM Role as r WHERE r.roleName=:roleName";
        Query query = entityManager.createQuery(stringQuery);
        query.setParameter("roleName", roleName);
        return (Role) query.getSingleResult();
    }
}