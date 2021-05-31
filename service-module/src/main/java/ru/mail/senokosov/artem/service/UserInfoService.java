package ru.mail.senokosov.artem.service;

import org.hibernate.service.spi.ServiceException;
import ru.mail.senokosov.artem.service.model.UserInfoDTO;

public interface UserInfoService {

    UserInfoDTO getUserByUserName(String userName);

    UserInfoDTO changeNameById(UserInfoDTO userInfoDTO);

    UserInfoDTO changeSurnameById(UserInfoDTO userInfoDTO);

    UserInfoDTO changeAddressById(UserInfoDTO userInfoDTO);

    UserInfoDTO changeTelephoneById(UserInfoDTO userInfoDTO);

    UserInfoDTO changePasswordById(UserInfoDTO userInfoDTO) throws ServiceException;
}
