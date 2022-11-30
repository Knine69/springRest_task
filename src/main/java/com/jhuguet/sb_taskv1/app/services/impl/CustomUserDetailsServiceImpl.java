package com.jhuguet.sb_taskv1.app.services.impl;

import com.jhuguet.sb_taskv1.app.exceptions.UsernameNotFound;
import com.jhuguet.sb_taskv1.app.models.User;
import com.jhuguet.sb_taskv1.app.repositories.UserRepository;
import com.jhuguet.sb_taskv1.app.services.CustomUserDetailsService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(UsernameNotFound::new);

        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername()).password(
                new BCryptPasswordEncoder().encode(user.getPassword())).roles(
                user.getUsername().equalsIgnoreCase("administrator") ? "ADMIN" : "USER").build();
    }

    public User getUserByUsername(String username) throws UsernameNotFound {
        return userRepository.findByUsername(username).orElseThrow(UsernameNotFound::new);
    }
}
