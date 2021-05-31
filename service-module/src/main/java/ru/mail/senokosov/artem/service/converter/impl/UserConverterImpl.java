package ru.mail.senokosov.artem.service.converter.impl;

import liquibase.pro.packaged.S;
import org.springframework.stereotype.Component;
import ru.mail.senokosov.artem.repository.model.Role;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.repository.model.UserInfo;
import ru.mail.senokosov.artem.service.converter.UserConverter;
import ru.mail.senokosov.artem.service.model.UserDTO;
import ru.mail.senokosov.artem.service.model.UserInfoDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class UserConverterImpl implements UserConverter {

    public List<UserDTO> convert(List<User> users) {
        return users.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    @Override
    public User convert(UserDTO userDTO) {
        User user = new User();
        String secondname = userDTO.getSecondname();
        user.setSecondname(secondname);
        String firstname = userDTO.getFirstname();
        user.setFirstname(firstname);
        String middlename = userDTO.getMiddlename();
        user.setMiddlename(middlename);
        String email = userDTO.getEmail();
        user.setEmail(email);
        return user;
    }

    @Override
    public UserDTO convert(User user) {
        UserDTO userDTO = new UserDTO();
        Long id = user.getId();
        userDTO.setId(id);
        String secondname = user.getSecondname();
        userDTO.setSecondname(secondname);
        String firstname = user.getFirstname();
        userDTO.setFirstname(firstname);
        String middlename = user.getMiddlename();
        userDTO.setMiddlename(middlename);
        String email = user.getEmail();
        userDTO.setEmail(email);
        String password = user.getPassword();
        userDTO.setPassword(password);
        Role role = user.getRole();
        userDTO.setRoleName(String.valueOf(role));
        return userDTO;
    }

    @Override
    public UserInfoDTO convertUserToUserDetailsDTO(User user) {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        Long id = user.getId();
        userInfoDTO.setId(id);
        String firstName = user.getFirstname();
        userInfoDTO.setFirstName(firstName);
        String lastName = user.getSecondname();
        userInfoDTO.setLastName(lastName);
        UserInfo userInfo = user.getUserInfo();
        if (Objects.nonNull(userInfo)) {
            String address = userInfo.getAddress();
            userInfoDTO.setAddress(address);
            String telephone = userInfo.getTelephone();
            userInfoDTO.setTelephone(telephone);
        }
        return userInfoDTO;
    }
}
