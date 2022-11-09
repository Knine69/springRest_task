package com.jhuguet.sb_taskv1.app.web.manager;

import com.jhuguet.sb_taskv1.app.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class DetailsManager {

    private final UserDetailsManager userDetailsManager;

    @Autowired
    public DetailsManager(UserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }

    public void saveUserDetails(User user, boolean isAdmin) {
        userDetailsManager.createUser(org.springframework.security.core.userdetails.User
                .withUsername(user
                        .getUsername()
                        .toLowerCase())
                .password("{noop}" + user
                        .getPassword()
                        .toLowerCase(Locale.ROOT))
                .roles(isAdmin ? "ADMIN" : "USER")
                .build());
    }

    public boolean validateUserExists(User user) {
        if (!userDetailsManager.userExists(user.getUsername())) {
            return false;
        }
        return true;
    }
}
