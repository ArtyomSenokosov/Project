package ru.mail.senokosov.artem.service.converter.impl;

import org.springframework.stereotype.Component;
import ru.mail.senokosov.artem.repository.model.Role;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.converter.UserConverter;
import ru.mail.senokosov.artem.service.model.UserDTO;

import java.util.UUID;

@Component
public class UserConverterImpl implements UserConverter {
    @Override
    public User convert(UserDTO userDTO, Role role) {
        User user = new User();
        String secondname = userDTO.getSecondname();
        user.setSecondname(secondname);
        String firstname = userDTO.getFirstname();
        user.setFirstname(firstname);
        String middlename = userDTO.getMiddlename();
        user.setMiddlename(middlename);
        String email = userDTO.getEmail();
        user.setEmail(email);
        String password = userDTO.getPassword();
        user.setPassword(password);
        user.setRole(role);
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
        userDTO.setRole(role);
        return userDTO;
    }
}
