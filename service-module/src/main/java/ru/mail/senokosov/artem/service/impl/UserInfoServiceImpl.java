package ru.mail.senokosov.artem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.service.spi.ServiceException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mail.senokosov.artem.repository.UserRepository;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.repository.model.UserInfo;
import ru.mail.senokosov.artem.service.UserInfoService;
import ru.mail.senokosov.artem.service.converter.UserConverter;
import ru.mail.senokosov.artem.service.model.UserInfoDTO;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserInfoDTO getUserByUserName(String userName) {
        User user = userRepository.findUserByEmail(userName);
        return userConverter.convertUserToUserDetailsDTO(user);
    }

    @Override
    public UserInfoDTO changeNameById(UserInfoDTO userInfoDTO) {
        User user = userRepository.findById(userInfoDTO.getId());
        String firstName = userInfoDTO.getFirstName();
        user.setFirstname(firstName);
        userRepository.merge(user);
        return userConverter.convertUserToUserDetailsDTO(user);
    }

    @Override
    public UserInfoDTO changeSurnameById(UserInfoDTO userInfoDTO) {
        User user = userRepository.findById(userInfoDTO.getId());
        String lastname = userInfoDTO.getLastName();
        user.setSecondname(lastname);
        userRepository.merge(user);
        return userConverter.convertUserToUserDetailsDTO(user);
    }

    @Override
    public UserInfoDTO changeAddressById(UserInfoDTO userInfoDTO) {
        User user = userRepository.findById(userInfoDTO.getId());
        String address = userInfoDTO.getAddress();
        UserInfo userInfo = user.getUserInfo();
        userInfo.setAddress(address);
        user.setUserInfo(userInfo);
        userRepository.merge(user);
        return userConverter.convertUserToUserDetailsDTO(user);
    }

    @Override
    public UserInfoDTO changeTelephoneById(UserInfoDTO userInfoDTO) {
        User user = userRepository.findById(userInfoDTO.getId());
        String telephone = userInfoDTO.getTelephone();
        UserInfo userInfo = user.getUserInfo();
        userInfo.setTelephone(telephone);
        user.setUserInfo(userInfo);
        userRepository.merge(user);
        return userConverter.convertUserToUserDetailsDTO(user);
    }

    @Override
    public UserInfoDTO changePasswordById(UserInfoDTO userInfoDTO) throws ServiceException {
        User user = userRepository.findById(userInfoDTO.getId());
        String oldPassword = userInfoDTO.getOldPassword();
        String userPassword = user.getPassword();
        if (passwordEncoder.matches(oldPassword, userPassword)) {
            String newPassword = passwordEncoder.encode(userInfoDTO.getNewPassword());
            user.setPassword(newPassword);
            userRepository.merge(user);
        } else {
            throw new ServiceException("The current password was entered incorrectly");
        }
        return userInfoDTO;
    }
}
