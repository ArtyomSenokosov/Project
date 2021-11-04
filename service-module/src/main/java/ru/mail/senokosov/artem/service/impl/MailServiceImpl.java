package ru.mail.senokosov.artem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.mail.senokosov.artem.service.MailService;
import ru.mail.senokosov.artem.service.model.UserDTO;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendPasswordToEmailAfterAddUser(UserDTO userDTO) {
        SimpleMailMessage message = getMailMessageForAddUser(userDTO, userDTO.getEmail());
        log.debug("Sending registration password email to: {}", userDTO.getEmail());
        javaMailSender.send(message);
        log.info("Registration password email sent successfully to: {}", userDTO.getEmail());
    }

    @Override
    public void sendPasswordToEmailAfterResetPassword(UserDTO userDTO) {
        SimpleMailMessage message = getMailMessageForResetPassword(userDTO, userDTO.getEmail());
        log.debug("Sending password reset email to: {}", userDTO.getEmail());
        javaMailSender.send(message);
        log.info("Password reset email sent successfully to: {}", userDTO.getEmail());
    }

    private SimpleMailMessage getMailMessageForAddUser(UserDTO userDTO, String recipientMail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientMail);
        message.setSubject("Your registration password");
        message.setText(String.format("Hello, your account %s has been successfully created. Please check your email for the password.", userDTO.getEmail()));
        log.debug("Creating registration email message for: {}", recipientMail);
        return message;
    }

    private SimpleMailMessage getMailMessageForResetPassword(UserDTO userDTO, String recipientMail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientMail);
        message.setSubject("Your new password");
        message.setText(String.format("Hello %s, your password has been successfully reset. Please check your email for the new password.", userDTO.getFirstName()));
        log.debug("Creating password reset email message for: {}", recipientMail);
        return message;
    }
}