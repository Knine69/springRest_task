package com.jhuguet.sb_taskv1.app.web.security;

import com.jhuguet.sb_taskv1.app.exceptions.WrongCredentials;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @SneakyThrows
    @Override
    public Authentication authenticate(Authentication authentication) {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails = giveUserDetails(username);

        if (userDetails != null) {
            return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
        } else {
            throw new WrongCredentials();
        }
    }

    public UserDetails giveUserDetails(String username) {
        return User.withUsername(username).password("*").roles(
                username.equalsIgnoreCase("administrator") ? "ADMIN" : "USER").build();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
