package ru.mail.senokosov.artem.service;

import org.springframework.data.domain.Page;
import ru.mail.senokosov.artem.repository.model.Role;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.model.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();

    Page<User> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

    User findUserByEmail(String email);

    void addUser(UserDTO userDTO, Role role);

    Optional<UserDTO> deleteUser(Long id);

    void changePassword(Long id);

    void changeRole(Long id, Role role);
}