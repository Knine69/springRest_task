package com.jhuguet.sb_taskv1.app.web.utils;

import com.jhuguet.sb_taskv1.app.exceptions.UsernameNotFound;
import com.jhuguet.sb_taskv1.app.models.AuthenticatedAccount;
import com.jhuguet.sb_taskv1.app.models.User;
import com.jhuguet.sb_taskv1.app.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    private UserRepository userRepository;

    @Autowired
    public JwtUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthenticatedAccount createJwt(String username) throws IOException, UsernameNotFound {
        Map<String, Object> claims = new HashMap<>();
        return assembleToken(claims, username);
    }

    public AuthenticatedAccount assembleToken(Map<String, Object> claims, String username) throws IOException,
            UsernameNotFound {
        User user = userRepository.findByUsername(username).orElseThrow(UsernameNotFound::new);
        claims.put("id", user.getId());
        return new AuthenticatedAccount(createToken(claims, username), new Date());
    }

    private String createToken(Map<String, Object> claims, String username) throws IOException {
        return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date()).setExpiration(
                new Date(System.currentTimeMillis() + 1000l * 60l * 10l)).signWith(
                Keys.hmacShaKeyFor(new FileInputStream("secret-key.pub").readAllBytes())).compact();
    }
}
