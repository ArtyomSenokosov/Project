package ru.mail.senokosov.artem.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.RoleRepository;
import ru.mail.senokosov.artem.repository.model.Role;

import javax.persistence.NoResultException;
import javax.persistence.Query;

@Slf4j
@Repository
public class RoleRepositoryImpl extends GenericRepositoryImpl<Long, Role> implements RoleRepository {

    @Override
    public Role findByRoleName(String roleName) {
        log.debug("Attempting to find Role by roleName: {}", roleName);
        String stringQuery = "SELECT r FROM Role as r WHERE r.roleName=:roleName";
        Query query = entityManager.createQuery(stringQuery);
        query.setParameter("roleName", roleName);
        Role role = null;
        try {
            role = (Role) query.getSingleResult();
            log.debug("Found role: {}", role);
        } catch (NoResultException exception) {
            log.info("No Role found with roleName: {}", roleName);
        } catch (Exception exception) {
            log.error("An error occurred while finding Role by roleName: {}", roleName, exception);
            throw exception;
        }
        return role;
    }
}