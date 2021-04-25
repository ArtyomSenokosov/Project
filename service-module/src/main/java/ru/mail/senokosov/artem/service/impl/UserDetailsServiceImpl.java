package ru.mail.senokosov.artem.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.UserService;
import ru.mail.senokosov.artem.service.model.UserLogin;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with email: %s was not found", email));
        }
        return new UserLogin(user);
    }
}
