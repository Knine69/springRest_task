package com.jhuguet.sb_taskv1.app.services.utils;

import com.jhuguet.sb_taskv1.app.models.AuthenticatedAccount;
import com.jhuguet.sb_taskv1.app.models.AuthenticationRequest;
import com.jhuguet.sb_taskv1.app.models.GiftCertificate;
import com.jhuguet.sb_taskv1.app.models.Order;
import com.jhuguet.sb_taskv1.app.models.Tag;
import com.jhuguet.sb_taskv1.app.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
public class SetUpUtils {
    public Tag sampleTag() {
        return new Tag("Cloud", new HashSet<>(sampleCertificates()));
    }

    public Map<String, String> sampleJwt() throws IOException {
        String token = sampleToken();
        return Map.of("authorization", "Bearer " + token);
    }

    public String sampleToken() throws IOException {
        return Jwts.builder().setClaims(new HashMap<>(Map.of("id", 1))).setSubject("username").setIssuedAt(new Date())
                   .setExpiration(new Date(System.currentTimeMillis() + 1000l * 60l * 10l)).signWith(
                        Keys.hmacShaKeyFor(new FileInputStream("secret-key.pub").readAllBytes())).compact();
    }

    public AuthenticationRequest sampleAuthRequest() {
        return new AuthenticationRequest("username", "password");
    }

    public AuthenticatedAccount sampleAuthAccount() throws IOException {
        return new AuthenticatedAccount(sampleToken(), new Date());
    }

    public Set<Tag> sampleTags() {
        return Set.of(sampleTag(), sampleTag());
    }

    public Page<GiftCertificate> samplePageCertificates() {
        return new PageImpl<>(new ArrayList<>(sampleCertificates()), PageRequest.of(1, 1), sampleCertificates().size());
    }

    public List<GiftCertificate> sampleCertificates() {
        return List.of(sampleCertificate(), sampleCertificate());
    }

    public GiftCertificate sampleCertificate() {
        return GiftCertificate.builder().name("AWS Certificate").description("AWS Master's certificate").price(
                BigDecimal.valueOf(8.99)).duration(10).lastUpdateDate("2022-09-20T14:33:15.1301054").createDate(
                "2022-09-20T14:33:15.1301054").associatedTags(new HashSet<>(List.of(new Tag("Cloud")))).build();
    }

    public Page<Tag> samplePageTags() {
        return new PageImpl<>(new ArrayList<>(sampleTags()), PageRequest.of(1, 1), sampleCertificates().size());
    }

    public Page<User> samplePageUsers() {
        return new PageImpl<>(new ArrayList<>(sampleUsers()), PageRequest.of(1, 1), sampleUsers().size());
    }

    public List<User> sampleUsers() {
        return List.of(sampleUser(), sampleUser());
    }

    public User sampleUser() {
        return new User(0, "username", "user@domain.com", "password", new HashSet<>(sampleOrders()), "USER");
    }

    public List<Order> sampleOrders() {
        return List.of(sampleOrder(), sampleOrder());
    }

    public Order sampleOrder() {
        Order order = new Order(1);
        order.addCertificate(sampleCertificate());
        return order;
    }

    public UserDetails sampleUserDetails() {
        return org.springframework.security.core.userdetails.User.withUsername("username").username("username")
                                                                 .password("password").roles("USER").build();
    }
}
