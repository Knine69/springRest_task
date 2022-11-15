package com.jhuguet.sb_taskv1.app.web.security;

import com.jhuguet.sb_taskv1.app.services.impl.CustomUserDetailsServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;
import java.util.Date;

@Component
public class JwtFilterChain extends OncePerRequestFilter {

    private CustomUserDetailsServiceImpl detailsService;

    @Autowired
    public JwtFilterChain(CustomUserDetailsServiceImpl detailsService) {
        this.detailsService = detailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        String authHeader = request.getHeader("Authorization");
        Key key = Keys.hmacShaKeyFor(new FileInputStream("secret-key.pub").readAllBytes());
        String username = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.split(" ")[1];
            username = (String) Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody()
                    .get("username");
        }

        if (username != null && SecurityContextHolder
                .getContext()
                .getAuthentication() == null) {

            UserDetails userDetails = detailsService.loadUserByUsername(username);

            if (validateRequest(username, userDetails, key, jwt)) {

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authToken);

            }
            filterChain.doFilter(request, response);
        }
    }

    private boolean validateRequest(String username, UserDetails details, Key key, String jwt) {
        return username.equals(details.getUsername()) && validateExpiration(key, jwt);
    }

    private boolean validateExpiration(Key key, String jwt) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody()
                .getExpiration()
                .before(new Date());
    }
}
