package ru.mail.senokosov.artem.service.converter;

import ru.mail.senokosov.artem.repository.model.Role;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.model.UserDTO;

public interface UserConverter {

    User convert(UserDTO userDTO, Role role);

    UserDTO convert(User user);
}
