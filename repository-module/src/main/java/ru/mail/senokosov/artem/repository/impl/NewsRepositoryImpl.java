package ru.mail.senokosov.artem.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.NewsRepository;
import ru.mail.senokosov.artem.repository.model.News;

import javax.persistence.Query;
import java.util.List;

@Slf4j
@Repository
public class NewsRepositoryImpl extends GenericRepositoryImpl<Long, News> implements NewsRepository {

    @Override
    @SuppressWarnings("unchecked")
    public List<News> findAll(Integer startPosition, Integer maximumNewsOnPage) {
        log.debug("Fetching news starting at position: {}, with maximum page size: {}",
                startPosition, maximumNewsOnPage);
        String hql = "SELECT n FROM News as n ORDER BY n.dateOfCreation DESC";
        Query query = entityManager.createQuery(hql);
        query.setFirstResult(startPosition);
        query.setMaxResults(maximumNewsOnPage);
        List<News> newsList;
        try {
            newsList = query.getResultList();
            log.debug("Fetched {} news items", newsList.size());
            return newsList;
        } catch (Exception exception) {
            log.error("Error occurred while fetching news items", exception);
            throw exception;
        }
    }
}