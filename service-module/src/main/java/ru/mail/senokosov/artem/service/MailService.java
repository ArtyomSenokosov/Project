package ru.mail.senokosov.artem.service;

import ru.mail.senokosov.artem.service.model.UserDTO;

public interface MailService {

    void sendPasswordToEmailAfterAddUser(UserDTO userDTO);

    void sendPasswordToEmailAfterResetPassword(UserDTO userDTO);
}