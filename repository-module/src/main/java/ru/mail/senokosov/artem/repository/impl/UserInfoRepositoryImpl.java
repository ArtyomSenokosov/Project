package ru.mail.senokosov.artem.repository.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.UserInfoRepository;
import ru.mail.senokosov.artem.repository.model.UserInfo;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import static ru.mail.senokosov.artem.repository.constant.RepositoryConstants.USER_ID_PARAMETER;

@Repository
@Log4j2
public class UserInfoRepositoryImpl extends GenericRepositoryImpl<Long, UserInfo> implements UserInfoRepository {

    @Override
    @SuppressWarnings("unchecked")
    public UserInfo findUserInfoByUserId(Long id) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<UserInfo> query = criteriaBuilder.createQuery(UserInfo.class);
            Root<UserInfo> userDetailsRoot = query.from(UserInfo.class);
            query.select(userDetailsRoot);
            ParameterExpression<Long> parameter = criteriaBuilder.parameter(Long.class);
            query.where(criteriaBuilder.equal(userDetailsRoot.get(USER_ID_PARAMETER), parameter));
            TypedQuery<UserInfo> typedQuery = entityManager.createQuery(query);
            typedQuery.setParameter(parameter, id);
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            log.info("User details does not exist");
            return null;
        }
    }
}
