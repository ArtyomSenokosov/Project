package ru.mail.senokosov.artem.service.converter;

import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.model.UserDTO;
import ru.mail.senokosov.artem.service.model.UserInfoDTO;

public interface UserConverter {

    UserDTO convert(User user);

    User convert(UserDTO userDTO);

    UserInfoDTO convertUserToUserInfoDTO(User user);
}