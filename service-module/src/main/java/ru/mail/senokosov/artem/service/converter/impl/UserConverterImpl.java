package ru.mail.senokosov.artem.service.converter.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.mail.senokosov.artem.repository.model.Role;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.repository.model.UserInfo;
import ru.mail.senokosov.artem.service.converter.UserConverter;
import ru.mail.senokosov.artem.service.model.UserDTO;
import ru.mail.senokosov.artem.service.model.UserInfoDTO;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserConverterImpl implements UserConverter {

    private final ModelMapper modelMapper;

    @Override
    public UserDTO convert(User user) {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        String middleName = user.getMiddleName();
        if (Objects.nonNull(middleName) && !middleName.isEmpty()) {
            userDTO.setMiddleName(middleName);
        }

        if (Objects.nonNull(user.getRole())) {
            Role role = user.getRole();
            String roleName = role.getRoleName();
            userDTO.setRoleName(roleName);
        }

        return userDTO;
    }

    @Override
    public User convert(UserDTO userDTO) {
        User user = new User();

        String lastName = userDTO.getLastName();
        user.setLastName(lastName);

        String firstName = userDTO.getFirstName();
        user.setFirstName(firstName);

        String middleName = userDTO.getMiddleName();
        if (Objects.nonNull(middleName) && !middleName.isEmpty()) {
            user.setMiddleName(middleName);
        } else {
            user.setMiddleName(null);
        }

        String email = userDTO.getEmail();
        user.setEmail(email);

        return user;
    }

    @Override
    public UserInfoDTO convertUserToUserInfoDTO(User user) {
        UserInfoDTO userInfoDTO = modelMapper.map(user, UserInfoDTO.class);

        String lastName = user.getLastName();
        userInfoDTO.setLastName(lastName);

        String firstName = user.getFirstName();
        userInfoDTO.setFirstName(firstName);

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