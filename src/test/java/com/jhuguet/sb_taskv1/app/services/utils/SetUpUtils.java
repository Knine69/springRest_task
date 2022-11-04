package com.jhuguet.sb_taskv1.app.services.utils;

import com.jhuguet.sb_taskv1.app.models.GiftCertificate;
import com.jhuguet.sb_taskv1.app.models.Order;
import com.jhuguet.sb_taskv1.app.models.Tag;
import com.jhuguet.sb_taskv1.app.models.User;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
public class SetUpUtils {
    public GiftCertificate sampleCertificate() {
        return GiftCertificate.builder()
                .name("AWS Certificate")
                .description("AWS Master's certificate")
                .price(BigDecimal.valueOf(8.99))
                .duration(10)
                .lastUpdateDate("2022-09-20T14:33:15.1301054")
                .createDate("2022-09-20T14:33:15.1301054")
                .associatedTags(new HashSet<>(List.of(new Tag("Cloud"))))
                .build();
    }

    public Tag sampleTag() {
        return new Tag("Cloud", new HashSet<>(sampleCertificates()));
    }


    public User sampleUser() {
        return new User(1, "user", "user@domain.com", new HashSet<>(sampleOrders()));
    }

    public List<User> sampleUsers() {
        return List.of(sampleUser(), sampleUser());
    }

    public Order sampleOrder() {
        Order order = new Order(1);
        order.addCertificate(sampleCertificate());
        return order;
    }

    public Set<Order> sampleOrders() {
        return Set.of(sampleOrder(), sampleOrder());
    }

    public Set<Tag> sampleTags() {
        return Set.of(
                sampleTag(),
                sampleTag()
        );
    }

    public List<GiftCertificate> sampleCertificates() {
        return List.of(
                sampleCertificate(),
                sampleCertificate()
        );
    }

    public Page<GiftCertificate> samplePageCertificates() {
        return new PageImpl<>(
                new ArrayList<>(sampleCertificates()), PageRequest.of(1, 1), sampleCertificates().size());
    }

    public Page<Tag> samplePageTags() {
        return new PageImpl<>(
                new ArrayList<>(sampleTags()), PageRequest.of(1, 1), sampleCertificates().size());
    }
}
