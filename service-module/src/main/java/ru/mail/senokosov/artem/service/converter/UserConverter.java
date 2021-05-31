package ru.mail.senokosov.artem.service.converter;

import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.model.UserDTO;
import ru.mail.senokosov.artem.service.model.UserInfoDTO;

import java.util.List;

public interface UserConverter {

    List<UserDTO> convert(List<User> users);

    User convert(UserDTO userDTO);

    UserDTO convert(User user);

    UserInfoDTO convertUserToUserDetailsDTO(User user);
}
