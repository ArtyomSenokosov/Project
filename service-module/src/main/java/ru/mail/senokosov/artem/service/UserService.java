package ru.mail.senokosov.artem.service;

import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.PageDTO;
import ru.mail.senokosov.artem.service.model.UserDTO;
import ru.mail.senokosov.artem.service.model.UserInfoDTO;

public interface UserService {

    PageDTO getUsersByPage(Integer page);

    boolean isDeleteById(Long id);

    UserDTO addUser(UserDTO userDTO) throws ServiceException;

    void changeRoleById(String roleName, Long id) throws ServiceException;

    void resetPassword(Long id) throws ServiceException;

    UserInfoDTO getUserByUserName(String userName) throws ServiceException;

    void changeParameterById(UserInfoDTO userInfoDTO) throws ServiceException;
}