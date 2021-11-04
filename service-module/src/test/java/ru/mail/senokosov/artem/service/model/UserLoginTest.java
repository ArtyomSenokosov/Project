package ru.mail.senokosov.artem.service.model;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.mail.senokosov.artem.repository.model.Role;
import ru.mail.senokosov.artem.repository.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserLoginTest {

    @Test
    void testUserDetailsImplementation() {
        Role userRole = new Role();
        userRole.setRoleName("ROLE_USER");
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("password");
        user.setRole(userRole);

        UserLogin userLogin = new UserLogin(user);

        assertEquals("user@example.com", userLogin.getUsername());
        assertEquals("password", userLogin.getPassword());
        assertTrue(userLogin.isAccountNonExpired());
        assertTrue(userLogin.isAccountNonLocked());
        assertTrue(userLogin.isCredentialsNonExpired());
        assertTrue(userLogin.isEnabled());

        GrantedAuthority expectedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        assertTrue(userLogin.getAuthorities().contains(expectedAuthority));
    }
}
