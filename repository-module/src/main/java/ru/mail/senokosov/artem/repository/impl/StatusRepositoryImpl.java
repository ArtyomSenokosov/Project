package ru.mail.senokosov.artem.repository.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.StatusRepository;
import ru.mail.senokosov.artem.repository.model.Status;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import static ru.mail.senokosov.artem.repository.constant.RepositoryConstants.ID_PARAMETER;

@Repository
@Log4j2
public class StatusRepositoryImpl extends GenericRepositoryImpl<Long, Status> implements StatusRepository {

    @Override
    public Status findByStatusName(String statusName) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Status> statusQuery = criteriaBuilder.createQuery(Status.class);
            Root<Status> statusRoot = statusQuery.from(Status.class);
            statusQuery.select(statusRoot);
            ParameterExpression<Long> parameter = criteriaBuilder.parameter(Long.class);
            statusQuery.where(criteriaBuilder.equal(statusRoot.get(ID_PARAMETER), parameter));
            TypedQuery<Status> typedQuery = entityManager.createQuery(statusQuery);
            typedQuery.setParameter(String.valueOf(parameter), statusName);
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}