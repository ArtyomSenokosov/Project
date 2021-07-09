package ru.mail.senokosov.artem.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.mail.senokosov.artem.service.model.enums.RoleDTOEnum;

import java.util.Collection;
import java.util.Collections;


@Profile("security")
@Configuration
public class TestUserDetailsServiceConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                switch (username) {
                    case "rest@gmail.com":
                        return getRestUserDetails();
                    case "admin@gmail.com":
                        return getAdminUserDetails();
                    case "seller@gmail.com":
                        return getSellerUserDetails();
                    case "customer@gmail.com":
                        return getCustomerUserDetails();
                    default:
                        throw new UsernameNotFoundException(String.format("User with %s was not found", username));
                }
            }

            private UserDetails getRestUserDetails() {
                return new UserDetails() {
                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        String role = RoleDTOEnum.SECURE_API_USER.name();
                        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
                        return Collections.singletonList(grantedAuthority);
                    }

                    @Override
                    public String getPassword() {
                        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                        return encoder.encode("test");
                    }

                    @Override
                    public String getUsername() {
                        return "rest@gmail.com";
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
                };
            }

            private UserDetails getAdminUserDetails() {
                return new UserDetails() {
                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        String role = RoleDTOEnum.ADMINISTRATOR.name();
                        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
                        return Collections.singletonList(grantedAuthority);
                    }

                    @Override
                    public String getPassword() {
                        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                        return encoder.encode("test");
                    }

                    @Override
                    public String getUsername() {
                        return "admin@gmail.com";
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
                };
            }

            private UserDetails getSellerUserDetails() {
                return new UserDetails() {
                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        String role = RoleDTOEnum.SALE_USER.name();
                        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
                        return Collections.singletonList(grantedAuthority);
                    }

                    @Override
                    public String getPassword() {
                        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                        return encoder.encode("test");
                    }

                    @Override
                    public String getUsername() {
                        return "seller@gmail.com";
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
                };
            }

            private UserDetails getCustomerUserDetails() {
                return new UserDetails() {
                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        String role = RoleDTOEnum.CUSTOMER_USER.name();
                        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
                        return Collections.singletonList(grantedAuthority);
                    }

                    @Override
                    public String getPassword() {
                        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                        return encoder.encode("test");
                    }

                    @Override
                    public String getUsername() {
                        return "customer@gmail.com";
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
                };
            }
        };
    }
}