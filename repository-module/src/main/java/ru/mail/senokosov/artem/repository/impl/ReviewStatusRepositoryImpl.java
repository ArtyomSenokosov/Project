package ru.mail.senokosov.artem.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.ReviewStatusRepository;
import ru.mail.senokosov.artem.repository.model.ReviewStatus;

import javax.persistence.NoResultException;
import javax.persistence.Query;

@Slf4j
@Repository
public class ReviewStatusRepositoryImpl extends GenericRepositoryImpl<Long, ReviewStatus> implements ReviewStatusRepository {

    @Override
    public ReviewStatus findByStatusName(String statusName) {
        log.debug("Attempting to find ReviewStatus by status name: {}", statusName);
        String hql = "SELECT s FROM ReviewStatus as s WHERE s.statusName=:statusName";
        Query query = entityManager.createQuery(hql);
        query.setParameter("statusName", statusName);
        ReviewStatus reviewStatus = null;
        try {
            reviewStatus = (ReviewStatus) query.getSingleResult();
            log.debug("Found ReviewStatus: {}", reviewStatus);
        } catch (NoResultException exception) {
            log.error("No ReviewStatus found with statusName: {}", statusName, exception);
        } catch (Exception exception) {
            log.error("An unexpected error occurred while finding ReviewStatus by statusName: {}",
                    statusName, exception);
            throw exception;
        }
        return reviewStatus;
    }
}