package ru.mail.senokosov.artem.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.mail.senokosov.artem.repository.UserRepository;
import ru.mail.senokosov.artem.repository.model.Role;
import ru.mail.senokosov.artem.repository.model.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void shouldLoadUserByUsernameWhenUserExists() {
        String username = "user@example.com";
        User user = new User();
        user.setEmail(username);

        Role userRole = new Role();
        userRole.setRoleName("USER");
        user.setRole(userRole);

        when(userRepository.findUserByEmail(username)).thenReturn(user);

        UserDetails result = userDetailsService.loadUserByUsername(username);

        assertNotNull(result, "Expected a non-null UserDetails object.");
        assertEquals(username, result.getUsername(), "Expected usernames to match.");
        assertEquals("USER", user.getRole().getRoleName(), "Expected roles to match.");
        verify(userRepository, times(1)).findUserByEmail(username);
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {
        String username = "nonexistent@example.com";

        when(userRepository.findUserByEmail(username)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username),
                "Expected UsernameNotFoundException for non-existent user.");

        verify(userRepository, times(1)).findUserByEmail(username);
    }
}
