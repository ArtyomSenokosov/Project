package ru.mail.senokosov.artem.repository.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.RoleRepository;
import ru.mail.senokosov.artem.repository.model.Role;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import static ru.mail.senokosov.artem.repository.constant.RepositoryConstants.ID_PARAMETER;

@Repository
@Log4j2
public class RoleRepositoryImpl extends GenericRepositoryImpl<Long, Role> implements RoleRepository {

    @Override
    public Role findByRoleName(String roleName) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Role> roleQuery = criteriaBuilder.createQuery(Role.class);
            Root<Role> roleRoot = roleQuery.from(Role.class);
            roleQuery.select(roleRoot);
            ParameterExpression<Long> parameter = criteriaBuilder.parameter(Long.class);
            roleQuery.where(criteriaBuilder.equal(roleRoot.get(ID_PARAMETER), parameter));
            TypedQuery<Role> typedQuery = entityManager.createQuery(roleQuery);
            typedQuery.setParameter(String.valueOf(parameter), roleName);
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}