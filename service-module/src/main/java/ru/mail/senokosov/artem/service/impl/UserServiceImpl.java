package ru.mail.senokosov.artem.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mail.senokosov.artem.repository.PagingAndSortingRepository;
import ru.mail.senokosov.artem.repository.UserRepository;
import ru.mail.senokosov.artem.repository.model.Role;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.UserService;
import ru.mail.senokosov.artem.service.config.PasswordGenerator;
import ru.mail.senokosov.artem.service.converter.UserConverter;
import ru.mail.senokosov.artem.service.model.UserDTO;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final PagingAndSortingRepository pagingAndSortingRepository;
    private final PasswordGenerator passwordGenerator;

    public UserServiceImpl(UserRepository userRepository, UserConverter userConverter, PagingAndSortingRepository pagingAndSortingRepository, PasswordGenerator passwordGenerator) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.pagingAndSortingRepository = pagingAndSortingRepository;
        this.passwordGenerator = passwordGenerator;
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }

    @Override
    public Page<User> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.pagingAndSortingRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public User findUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }


    @Override
    @Transactional
    public void addUser(UserDTO userDTO, Role role) {
        User user = userConverter.convert(userDTO, role);
        userRepository.persist(user);
    }

    @Override
    @Transactional
    public Optional<UserDTO> deleteUser(Long id) {
        User user = userRepository.findById(id);
        userRepository.remove(user);
        return Optional.empty();
    }

    @Override
    @Transactional
    public void changePassword(Long id) {
        User user = userRepository.findById(id);
        String newPassword = passwordGenerator.generateStrongPassword();
        //BCrypt password = new BCrypt(new BCryptPasswordEncoder().upgradeEncoding());
        user.setPassword(newPassword);
        userRepository.merge(user);
        userRepository.remove(user);
    }

    @Override
    public void changeRole(Long id, Role role) {
        User user = userRepository.findById(id);
        user.setRole(role);
        userRepository.merge(user);
        userRepository.remove(user);
    }
}
