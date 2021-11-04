package ru.mail.senokosov.artem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.mail.senokosov.artem.repository.RoleRepository;
import ru.mail.senokosov.artem.repository.UserRepository;
import ru.mail.senokosov.artem.repository.model.Role;
import ru.mail.senokosov.artem.repository.model.User;
//import ru.mail.senokosov.artem.service.MailService;
import ru.mail.senokosov.artem.repository.model.UserInfo;
import ru.mail.senokosov.artem.service.UserService;
import ru.mail.senokosov.artem.service.converter.UserConverter;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.PageDTO;
import ru.mail.senokosov.artem.service.model.PaginationResult;
import ru.mail.senokosov.artem.service.model.UserDTO;
import ru.mail.senokosov.artem.service.model.UserInfoDTO;
import ru.mail.senokosov.artem.service.util.PaginationUtil;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.mail.senokosov.artem.service.constant.UserValidationConstant.MAXIMUM_USERS_ON_PAGE;
import static ru.mail.senokosov.artem.service.util.ServiceUtil.generateRandomPassword;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;
    //private final MailService mailService;

    @Override
    @Transactional
    public PageDTO getUsersByPage(Integer page) {
        log.debug("Requesting page number {} for users.", page);

        Long countUsers = userRepository.getCount();

        PaginationResult pagination = PaginationUtil.calculatePagination(countUsers, MAXIMUM_USERS_ON_PAGE, page);

        List<User> users = userRepository.findAll(pagination.getStartPosition(), MAXIMUM_USERS_ON_PAGE);
        log.debug("Retrieved {} users for page {}.", users.size(), pagination.getCurrentPage());

        List<UserDTO> userDTOs = users.stream()
                .map(userConverter::convert)
                .collect(Collectors.toList());

        PageDTO pageDTO = new PageDTO();
        pageDTO.setTotalPages(pagination.getTotalPages());
        pageDTO.setCurrentPage((long) pagination.getCurrentPage());
        pageDTO.setUsers(userDTOs);
        pageDTO.setStartPosition(pagination.getStartPosition());

        log.info("Page {} of users served with {} users.", pagination.getCurrentPage(), userDTOs.size());
        return pageDTO;
    }

    @Override
    @Transactional
    public boolean isDeleteById(Long id) {
        try {
            if (Objects.nonNull(userRepository.findById(id))) {
                userRepository.removeById(id);
                log.info("User with id {} has been successfully deleted.", id);
                return true;
            } else {
                log.warn("User with id {} not found. Deletion cannot be performed.", id);
                return false;
            }
        } catch (Exception exception) {
            log.error("Error occurred during the deletion of user with id {}: {}", id, exception.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public UserDTO addUser(UserDTO userDTO) throws ServiceException {
        log.debug("Starting process to add a new user with email: {}", userDTO.getEmail());

        User userByEmail = userRepository.findUserByEmail(userDTO.getEmail());
        if (Objects.isNull(userByEmail)) {
            String roleName = userDTO.getRoleName();
            Role role = roleRepository.findByRoleName(roleName);
            if (Objects.nonNull(role)) {
                User user = userConverter.convert(userDTO);
                user.setRole(role);
                String randomPassword = generateRandomPassword();
                String encodePassword = passwordEncoder.encode(randomPassword);
                user.setPassword(encodePassword);
                userRepository.persist(user);
                log.info("User with email: {} has been successfully added", userDTO.getEmail());

                UserDTO showUserDTO = userConverter.convert(user);
                showUserDTO.setPassword(randomPassword);
                // mailService.sendPasswordToEmailAfterAddUser(showUserDTO);
            } else {
                log.error("Role with name: {} was not found", roleName);
                throw new ServiceException(String.format("Role with name: %s was not found", roleName));
            }
        } else {
            log.error("User with email: {} already exists", userDTO.getEmail());
            throw new ServiceException(String.format("User with email: %s already exists", userDTO.getEmail()));
        }
        return userDTO;
    }

    @Override
    @Transactional
    public void changeRoleById(String roleName, Long id) throws ServiceException {
        log.debug("Attempting to change role to '{}' for user with id: {}", roleName, id);

        Role roleByRoleName = roleRepository.findByRoleName(roleName);
        if (Objects.nonNull(roleByRoleName)) {
            User user = userRepository.findById(id);
            if (Objects.nonNull(user)) {
                user.setRole(roleByRoleName);
                userRepository.merge(user);
                log.info("Successfully changed the role of user with id: {} to '{}'", id, roleName);
            } else {
                log.warn("User with id: {} was not found", id);
                throw new ServiceException(String.format("User with id: %s was not found", id));
            }
        } else {
            log.warn("Role with name: '{}' was not found", roleName);
            throw new ServiceException(String.format("Role with name: %s was not found", roleName));
        }
    }

    @Override
    @Transactional
    public void resetPassword(Long id) throws ServiceException {
        log.debug("Starting process to reset password for user with id: {}", id);

        User user = userRepository.findById(id);
        if (Objects.nonNull(user)) {
            String randomPassword = generateRandomPassword();
            String encodePassword = passwordEncoder.encode(randomPassword);
            user.setPassword(encodePassword);
            userRepository.merge(user);

            UserDTO showUserDTO = userConverter.convert(user);
            showUserDTO.setPassword(randomPassword);
            log.info("Password for user with id: {} has been successfully reset.", id);

            // mailService.sendPasswordToEmailAfterResetPassword(showUserDTO);
        } else {
            log.error("User with id: {} was not found, unable to reset password.", id);
            throw new ServiceException(String.format("User with id: %s was not found", id));
        }
    }

    @Override
    @Transactional
    public UserInfoDTO getUserByUserName(String userName) throws ServiceException {
        log.debug("Attempting to find user by username: {}", userName);

        User user = userRepository.findUserByEmail(userName);
        if (Objects.nonNull(user)) {
            log.info("User with username: {} found and being converted.", userName);
            return userConverter.convertUserToUserInfoDTO(user);
        } else {
            log.warn("User with username: {} was not found.", userName);
            throw new ServiceException(String.format("User with username: %s was not found", userName));
        }
    }

    @Override
    @Transactional
    public void changeParameterById(UserInfoDTO userInfoDTO) throws ServiceException {
        log.debug("Starting process to update parameters for user with id: {}", userInfoDTO.getId());

        if (userInfoDTO.getId() == null) {
            log.error("Attempt to update user parameters without specifying a user id.");
            throw new ServiceException("User id is required for updating profile");
        }

        User user = userRepository.findById(userInfoDTO.getId());
        if (Objects.nonNull(user)) {
            User changeUser = changeUserFields(userInfoDTO, user);
            userRepository.merge(changeUser);
            log.info("Successfully updated parameters for user with id: {}", userInfoDTO.getId());
        } else {
            log.warn("User with id: {} was not found, unable to update parameters.", userInfoDTO.getId());
            throw new ServiceException(String.format("User with id: %s was not found", userInfoDTO.getId()));
        }
    }

    private User changeUserFields(UserInfoDTO addUserInfoDTO, User user) throws ServiceException {
        String firstName = addUserInfoDTO.getFirstName();
        if (StringUtils.hasText(firstName)) {
            user.setFirstName(firstName);
        }
        String lastName = addUserInfoDTO.getLastName();
        if (StringUtils.hasText(lastName)) {
            user.setLastName(lastName);
        }
        String address = addUserInfoDTO.getAddress();
        if (StringUtils.hasText(address)) {
            UserInfo userInfo = user.getUserInfo();
            userInfo.setAddress(address);
            user.setUserInfo(userInfo);
        }
        String telephone = addUserInfoDTO.getTelephone();
        if (StringUtils.hasText(telephone)) {
            UserInfo userInfo = user.getUserInfo();
            userInfo.setTelephone(telephone);
            user.setUserInfo(userInfo);
        }
        String oldPassword = addUserInfoDTO.getOldPassword();
        String newPassword = addUserInfoDTO.getNewPassword();
        if (StringUtils.hasText(oldPassword) && StringUtils.hasText(newPassword)) {
            String userPassword = user.getPassword();
            if (passwordEncoder.matches(oldPassword, userPassword)) {
                log.info(String.valueOf(passwordEncoder.matches(oldPassword, userPassword)));
                String encodePassword = passwordEncoder.encode(addUserInfoDTO.getNewPassword());
                user.setPassword(encodePassword);
            } else {
                log.warn("Failed password update attempt for user ID {}: Old password does not match.", user.getId());
                throw new ServiceException("The current password was entered incorrectly.");
            }
        }
        return user;
    }
}