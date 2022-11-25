package com.jhuguet.sb_taskv1.app.web.security;

import com.jhuguet.sb_taskv1.app.exceptions.UsernameNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.WrongCredentials;
import com.jhuguet.sb_taskv1.app.repositories.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public CustomAuthenticationProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @SneakyThrows
    @Override
    public Authentication authenticate(Authentication authentication) {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        com.jhuguet.sb_taskv1.app.models.User user = userRepository.findByUsername(username);
        if (user != null) {
            UserDetails userDetails = giveUserDetails(username);

            if (matchPasswords(password, user.getPassword())) {
                return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
            } else {
                throw new WrongCredentials();
            }

        } else {
            throw new UsernameNotFound();
        }
    }

    public UserDetails giveUserDetails(String username) {
        return User.withUsername(username).password("*").roles(
                username.equalsIgnoreCase("administrator") ? "ADMIN" : "USER").build();
    }

    private boolean matchPasswords(String rawPassword, String storedPassword) {
        if (!passwordEncoder.matches(rawPassword, storedPassword)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
