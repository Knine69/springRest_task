package com.jhuguet.sb_taskv1.app.web.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    public String createJwt(String username) throws IOException {
        Map<String, Object> claims = new HashMap<>();
        return assembleToken(claims, username);
    }

    public String assembleToken(Map<String, Object> claims, String username) throws IOException {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() * 1000l * 60l * 10l))
                .signWith(Keys.hmacShaKeyFor(new FileInputStream("secret-key.pub").readAllBytes()))
                .compact();
    }
}
