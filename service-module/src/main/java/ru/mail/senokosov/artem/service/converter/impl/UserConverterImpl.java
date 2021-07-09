package ru.mail.senokosov.artem.service.converter.impl;

import org.springframework.stereotype.Component;
import ru.mail.senokosov.artem.repository.model.Role;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.repository.model.UserInfo;
import ru.mail.senokosov.artem.service.converter.UserConverter;
import ru.mail.senokosov.artem.service.model.add.AddUserDTO;
import ru.mail.senokosov.artem.service.model.show.ShowUserDTO;
import ru.mail.senokosov.artem.service.model.show.ShowUserInfoDTO;

import java.util.Objects;

@Component
public class UserConverterImpl implements UserConverter {

    @Override
    public ShowUserDTO convert(User user) {
        ShowUserDTO showUserDTO = new ShowUserDTO();
        Long id = user.getId();
        showUserDTO.setId(id);
        String lastName = user.getLastName();
        showUserDTO.setLastName(lastName);
        String firstName = user.getFirstName();
        showUserDTO.setFirstName(firstName);
        String middleName = user.getMiddleName();
        showUserDTO.setMiddleName(middleName);
        String email = user.getEmail();
        showUserDTO.setEmail(email);

        if (Objects.nonNull(user.getRole())) {
            Role role = user.getRole();
            String roleName = role.getRoleName();
            showUserDTO.setRoleName(roleName);
        }
        return showUserDTO;
    }

    @Override
    public User convert(AddUserDTO addUserDTO) {
        User user = new User();
        String lastName = addUserDTO.getLastName();
        user.setLastName(lastName);
        String firstName = addUserDTO.getFirstName();
        user.setFirstName(firstName);
        String middleName = addUserDTO.getMiddleName();
        user.setMiddleName(middleName);
        String email = addUserDTO.getEmail();
        user.setEmail(email);
        UserInfo userInfo = new UserInfo();
        String address = addUserDTO.getAddress();
        userInfo.setAddress(address);
        String telephone = addUserDTO.getTelephone();
        userInfo.setTelephone(telephone);
        userInfo.setUser(user);
        user.setUserInfo(userInfo);
        return user;
    }

    @Override
    public ShowUserInfoDTO convertUserToUserDetailsDTO(User user) {
        ShowUserInfoDTO showUserInfoDTO = new ShowUserInfoDTO();
        Long id = user.getId();
        showUserInfoDTO.setId(id);
        String firstName = user.getFirstName();
        showUserInfoDTO.setFirstName(firstName);
        String lastName = user.getLastName();
        showUserInfoDTO.setLastName(lastName);
        UserInfo userInfo = (UserInfo) user.getUserInfo();
        if (Objects.nonNull(userInfo)) {
            String address = userInfo.getAddress();
            showUserInfoDTO.setAddress(address);
            String telephone = userInfo.getTelephone();
            showUserInfoDTO.setTelephone(telephone);
        }
        return showUserInfoDTO;
    }
}