package ru.mail.senokosov.artem.service;

import org.hibernate.service.spi.ServiceException;
import org.springframework.data.domain.Page;
import org.springframework.mail.SimpleMailMessage;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.model.*;
import ru.mail.senokosov.artem.service.model.add.AddUserDTO;

import java.util.List;

public interface UserService {

    Page<User> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

    List<UserDTO> getAllUsers();

    User findUserByEmail(String email);

    UserDTO getUserById(Long id);

    SimpleMailMessage persist(UserDTO userDTO) throws ServiceException;

    void addUserAndSendPasswordToEmail(UserDTO userDTO) throws ServiceException;

    boolean deleteUserById(Long id) throws SecurityException;

    SimpleMailMessage resetPassword(Long id);

    UserDTO changeRoleById(UserDTO userDTO, Long roleId) throws ServiceException;

    void resetPasswordAndSendToEmail(Long id);
}