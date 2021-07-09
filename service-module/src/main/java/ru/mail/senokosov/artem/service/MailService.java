package ru.mail.senokosov.artem.service;

import ru.mail.senokosov.artem.service.model.show.ShowUserDTO;

public interface MailService {

    void sendPasswordToEmailAfterAddUser(ShowUserDTO userDTO);

    void sendPasswordToEmailAfterResetPassword(ShowUserDTO userDTO);
}