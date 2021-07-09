package ru.mail.senokosov.artem.service.converter;

import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.model.add.AddUserDTO;
import ru.mail.senokosov.artem.service.model.show.ShowUserDTO;
import ru.mail.senokosov.artem.service.model.show.ShowUserInfoDTO;

public interface UserConverter {

    ShowUserDTO convert(User user);

    User convert(AddUserDTO addUserDTO);

    ShowUserInfoDTO convertUserToUserDetailsDTO(User user);
}