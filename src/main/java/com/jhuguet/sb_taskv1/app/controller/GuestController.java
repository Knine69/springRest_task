package com.jhuguet.sb_taskv1.app.controller;

import com.jhuguet.sb_taskv1.app.models.AuthenticationRequest;
import com.jhuguet.sb_taskv1.app.web.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class GuestController {

    private JwtUtils jwtUtils;
    private AuthenticationManager manager;

    @Autowired
    public GuestController(JwtUtils jwtUtils, AuthenticationManager manager) {
        this.jwtUtils = jwtUtils;
        this.manager = manager;
    }

    @PostMapping("/authenticate")
    public String getJwt(@RequestBody AuthenticationRequest auth) throws IOException {
        manager.authenticate(new UsernamePasswordAuthenticationToken(auth.getUsername(), auth.getPassword()));

        return jwtUtils.createJwt(auth.getUsername());
    }
}
