package ru.mail.senokosov.artem.service;

import org.hibernate.service.spi.ServiceException;
import ru.mail.senokosov.artem.service.model.*;
import ru.mail.senokosov.artem.service.model.add.AddUserDTO;
import ru.mail.senokosov.artem.service.model.add.AddUserInfoDTO;
import ru.mail.senokosov.artem.service.model.show.ShowUserDTO;
import ru.mail.senokosov.artem.service.model.show.ShowUserInfoDTO;

public interface UserService {

    PageDTO getUsersByPage(Integer page);

    ShowUserDTO persist(AddUserDTO addUserDTO) throws ServiceException;

    boolean isDeleteById(Long id);

    ShowUserDTO resetPassword(Long id) throws ServiceException;

    ShowUserDTO changeRoleById(String roleName, Long id) throws ServiceException;

    ShowUserInfoDTO getUserByUserName(String userName) throws ServiceException;

    ShowUserInfoDTO changeParameterById(AddUserInfoDTO addUserInfoDTO) throws ServiceException;
}