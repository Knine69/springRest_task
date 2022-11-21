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
        String username = givePropertyValue("sub", jwt.split(" ")[1]);
        if (!authContainsAdmin(detailsService.loadUserByUsername(username))) {
            throw new UnqualifiedAuthority();
        }
    }

    private String givePropertyValue(String property, String jwt) throws IOException {
        return String.valueOf(Jwts.parserBuilder().setSigningKey(giveSignKey()).build().parseClaimsJws(jwt).getBody()
                                  .get(property));
    }

    private boolean authContainsAdmin(UserDetails user) {
        return user.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equalsIgnoreCase("ROLE_ADMIN"));
    }

    private Key giveSignKey() throws IOException {
        return Keys.hmacShaKeyFor(new FileInputStream("secret-key.pub").readAllBytes());
    }

    public void confirmUser(int givenId, String jwt) throws IOException, NotAuthorized {
        jwt = jwt.split(" ")[1];
        String username = givePropertyValue("sub", jwt);
        if (!username.equalsIgnoreCase("administrator")) {
            String id = givePropertyValue("id", jwt);
            if (Integer.parseInt(id) != givenId) {
                throw new NotAuthorized();
            }
        }
    }

}
