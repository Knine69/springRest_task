package com.jhuguet.sb_taskv1.app.controller;

import com.jhuguet.sb_taskv1.app.exceptions.MissingEntity;
import com.jhuguet.sb_taskv1.app.exceptions.MissingRequiredFields;
import com.jhuguet.sb_taskv1.app.models.AuthenticationRequest;
import com.jhuguet.sb_taskv1.app.models.User;
import com.jhuguet.sb_taskv1.app.services.UserService;
import com.jhuguet.sb_taskv1.app.web.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(path = "/")
public class GuestController {

    private JwtUtils jwtUtils;
    private AuthenticationManager manager;

    private UserService userService;

    @Autowired
    public GuestController(JwtUtils jwtUtils, AuthenticationManager manager, UserService userService) {
        this.jwtUtils = jwtUtils;
        this.manager = manager;
        this.userService = userService;
    }

    @PostMapping("login")
    public String login(@RequestBody AuthenticationRequest auth) throws IOException {
        manager.authenticate(new UsernamePasswordAuthenticationToken(auth.getUsername(), auth.getPassword()));

        return jwtUtils.createJwt(auth.getUsername());
    }

    @PostMapping("signin")
    public String signIn(@RequestBody User user) throws IOException, MissingRequiredFields, MissingEntity {
        userService.signIn(user);
        manager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        return jwtUtils.createJwt(user.getUsername());

    }
}
