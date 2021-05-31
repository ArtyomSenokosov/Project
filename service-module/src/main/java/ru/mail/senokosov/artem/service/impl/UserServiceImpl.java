package ru.mail.senokosov.artem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.bytebuddy.utility.RandomString;
import org.hibernate.service.spi.ServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mail.senokosov.artem.repository.PagingAndSortingRepository;
import ru.mail.senokosov.artem.repository.RoleRepository;
import ru.mail.senokosov.artem.repository.UserRepository;
import ru.mail.senokosov.artem.repository.model.Role;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.MailSender;
import ru.mail.senokosov.artem.service.UserService;
import ru.mail.senokosov.artem.service.config.PasswordGenerator;
import ru.mail.senokosov.artem.service.converter.UserConverter;
import ru.mail.senokosov.artem.service.model.UserDTO;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static ru.mail.senokosov.artem.service.constant.UserServiceConstants.PASSWORD_LENGTH;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final ThreadLocal<PagingAndSortingRepository> pagingAndSortingRepository = new ThreadLocal<PagingAndSortingRepository>();
    private final JavaMailSender javaMailSender;
    private final PasswordGenerator passwordGenerator;
    private final MailSender mailSender;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Page<User> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.pagingAndSortingRepository.get().findAll(pageable);
    }

    @Override
    @Transactional
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id);
        return userConverter.convert(user);
    }

    @Override
    public SimpleMailMessage persist(UserDTO userDTO) throws ServiceException {
        User userByEmail = userRepository.findUserByEmail(userDTO.getEmail());
        SimpleMailMessage message = new SimpleMailMessage();
        if (Objects.isNull(userByEmail)) {
            User user = userConverter.convert(userDTO);
            String password = RandomString.make(PASSWORD_LENGTH);
            String encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);
            UUID uuid = UUID.randomUUID();
            user.setUuid(uuid);

            Role role = roleRepository.findById(Long.valueOf(userDTO.getRoleName()));
            if (Objects.nonNull(role)) {
                user.setRole(role);
            } else {
                throw new ServiceException("Role does not exist");
            }
            userRepository.persist(user);
            if (!user.getEmail().isBlank()) {
                mailSender.send(user.getEmail(), password);
            }
        } else {
            throw new ServiceException("User with email: " + userDTO.getEmail() + " already exist");
        }
        return message;
    }

    @Override
    public void addUserAndSendPasswordToEmail(UserDTO userDTO) throws ServiceException {
        SimpleMailMessage message = persist(userDTO);
        if (Objects.nonNull(message)) {
            javaMailSender.send(message);
        }
    }

    @Override
    public boolean deleteUserById(Long id) throws SecurityException {
        User user = userRepository.findById(id);
        if (Objects.nonNull(user)) {
            userRepository.remove(user);
            return true;
        } else {
            throw new ServiceException("User does not exist");
        }
    }

    @Override
    public SimpleMailMessage resetPassword(Long id) {
        User user = userRepository.findById(id);
        SimpleMailMessage message = new SimpleMailMessage();
        if (Objects.nonNull(user)) {
            String randomPassword = passwordGenerator.generateStrongPassword();
            String encodePassword = passwordEncoder.encode(randomPassword);
            user.setPassword(encodePassword);
            String firstName = user.getFirstname();
            String email = user.getEmail();
            message = getMailMessageForResetPassword(firstName, randomPassword, email);
            userRepository.merge(user);
        }
        return message;
    }

    @Override
    public UserDTO changeRoleById(UserDTO userDTO, Long roleId) throws ServiceException {
        User user = userRepository.findById(userDTO.getId());
        if (Objects.nonNull(user)) {
            Role role = roleRepository.findById(roleId);
            user.setRole(role);
            userRepository.merge(user);
        } else {
            throw new ServiceException("User does not exist");
        }
        return userConverter.convert(user);
    }

    @Override
    public void resetPasswordAndSendToEmail(Long id) {
        SimpleMailMessage message = resetPassword(id);
        if (Objects.nonNull(message)) {
            javaMailSender.send(message);
        }
    }

    private SimpleMailMessage getMailMessageForAddUser(String email, String randomPassword, String recipientMail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientMail);
        message.setSubject("Your registration password");
        message.setText("Hello, your account " + email + " has been successfully created:" +
                "your password: " + randomPassword);
        return message;
    }

    private SimpleMailMessage getMailMessageForResetPassword(String firsName, String randomPassword, String recipientMail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientMail);
        message.setSubject("Your new password");
        message.setText("Hello " + firsName + ", your password has been successfully reset:" +
                "your new password: " + randomPassword);
        return message;
    }
}
