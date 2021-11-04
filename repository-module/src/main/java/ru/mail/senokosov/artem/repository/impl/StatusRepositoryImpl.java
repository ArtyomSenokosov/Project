package ru.mail.senokosov.artem.repository.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.StatusRepository;
import ru.mail.senokosov.artem.repository.model.Status;

import javax.persistence.NoResultException;
import javax.persistence.Query;

@Repository
@Log4j2
public class StatusRepositoryImpl extends GenericRepositoryImpl<Long, Status> implements StatusRepository {

    @Override
    public Status findByStatusName(String statusName) {
        String hql = "SELECT s FROM Status as s WHERE s.status=:statusName";
        Query query = entityManager.createQuery(hql);
        query.setParameter("statusName", statusName);
        try {
            query.getSingleResult();
        } catch (NoResultException e) {
            log.error(e.getMessage(), e);
            return null;
        }
        return (Status) query.getSingleResult();
    }
}