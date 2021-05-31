package ru.mail.senokosov.artem.repository;

import ru.mail.senokosov.artem.repository.model.UserInfo;

public interface UserInfoRepository extends GenericRepository<Long, UserInfo> {

    UserInfo findUserInfoByUserId(Long id);
}
