package ru.mail.senokosov.artem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.mail.senokosov.artem.repository.UserRepository;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.model.UserLogin;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @PostConstruct
    private void init() {
        log.info("UserDetailsServiceImpl instantiated");
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Attempting to load user by username: {}", username);
        User user = userRepository.findUserByEmail(username);
        if (Objects.nonNull(user)) {
            log.info("User with username: {} found with role: {}", user.getEmail(), user.getRole());
            return new UserLogin(user);
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}