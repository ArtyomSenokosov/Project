package ru.mail.senokosov.artem.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import ru.mail.senokosov.artem.service.model.UserDTO;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private MailServiceImpl mailService;

    @Test
    void shouldSendPasswordToEmailAfterAddUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("user@example.com");

        mailService.sendPasswordToEmailAfterAddUser(userDTO);

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void shouldSendPasswordToEmailAfterResetPassword() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("user@example.com");
        userDTO.setFirstName("Test");

        mailService.sendPasswordToEmailAfterResetPassword(userDTO);

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void shouldCreateCorrectMailMessageForAddUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("user@example.com");

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        mailService.sendPasswordToEmailAfterAddUser(userDTO);

        verify(javaMailSender).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertEquals("Your registration password", sentMessage.getSubject());
        assertTrue(Objects.requireNonNull(sentMessage.getText()).contains("has been successfully created"));
    }

    @Test
    void shouldCreateCorrectMailMessageForResetPassword() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("user@example.com");
        userDTO.setFirstName("John");

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        mailService.sendPasswordToEmailAfterResetPassword(userDTO);

        verify(javaMailSender).send(messageCaptor.capture());
        SimpleMailMessage sentMessage = messageCaptor.getValue();

        assertEquals("user@example.com", Objects.requireNonNull(sentMessage.getTo())[0]);
        assertEquals("Your new password", sentMessage.getSubject());
        assertTrue(Objects.requireNonNull(sentMessage.getText()).contains("John, your password has been successfully reset."));
    }
}
