package ru.mail.senokosov.artem.service.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.mail.senokosov.artem.repository.model.Role;
import ru.mail.senokosov.artem.repository.model.User;

import java.util.Collection;
import java.util.HashSet;

public class UserLogin implements UserDetails {

    private User user;//UserDTO

    public UserLogin(User user) {//userrepository
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {//поиск из базы данных finduser
        Collection<GrantedAuthority> authorities = new HashSet<>();
        Role role = user.getRole();
        GrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role.getRole().name());
        authorities.add(simpleGrantedAuthority);
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
