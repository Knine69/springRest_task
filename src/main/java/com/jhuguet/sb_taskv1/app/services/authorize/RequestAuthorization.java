package com.jhuguet.sb_taskv1.app.services.authorize;

import com.jhuguet.sb_taskv1.app.exceptions.NotAuthorized;
import com.jhuguet.sb_taskv1.app.exceptions.UnqualifiedAuthority;
import com.jhuguet.sb_taskv1.app.services.impl.CustomUserDetailsServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;

public class RequestAuthorization {

    private final CustomUserDetailsServiceImpl detailsService;

    @Autowired
    public RequestAuthorization(CustomUserDetailsServiceImpl detailsService) {
        this.detailsService = detailsService;
    }


    public void confirmRoles(String jwt) throws IOException, UnqualifiedAuthority {
        String username = String.valueOf(Jwts
                .parserBuilder()
                .setSigningKey(giveSignKey())
                .build()
                .parseClaimsJws(jwt.split(" ")[1])
                .getBody()
                .get("sub"));
        UserDetails user = detailsService.loadUserByUsername(username);
        if (!user
                .getAuthorities()
                .contains("ROLE_ADMIN")) {
            throw new UnqualifiedAuthority();
        }
    }

    private Key giveSignKey() throws IOException {
        return Keys.hmacShaKeyFor(new FileInputStream("secret-key.pub").readAllBytes());
    }

    public void confirmUser(int givenId, String jwt) throws IOException, NotAuthorized {
        String id = String.valueOf(Jwts
                .parserBuilder()
                .setSigningKey(giveSignKey())
                .build()
                .parseClaimsJws(jwt.split(" ")[1])
                .getBody()
                .get("id"));
        if (Integer.parseInt(id) != givenId) {
            throw new NotAuthorized();
        }
    }


}
