package com.jhuguet.sb_taskv1.app;

import com.jhuguet.sb_taskv1.app.models.GiftCertificate;
import com.jhuguet.sb_taskv1.app.models.Order;
import com.jhuguet.sb_taskv1.app.models.Tag;
import com.jhuguet.sb_taskv1.app.models.User;
import com.jhuguet.sb_taskv1.app.repositories.GiftCertificateRepository;
import com.jhuguet.sb_taskv1.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;

public class BootUpInfo {

    private final UserRepository userRepository;
    private final GiftCertificateRepository giftCertificateRepository;


    @Autowired
    public BootUpInfo(UserRepository userRepository, GiftCertificateRepository giftCertificateRepository) {
        this.userRepository = userRepository;
        this.giftCertificateRepository = giftCertificateRepository;
    }

    public void prepareInfo() {
        for (int i = 0; i < 10000; i++) {
            if (i < 1001) {
                addAll(i);
            } else {
                addCertificate(i);
            }
        }
    }

    private void addAll(int i) {
        String localDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        GiftCertificate certificate = new GiftCertificate(
                "Certificate" + i,
                "Description" + i,
                BigDecimal.valueOf(10.00),
                10,
                localDate,
                localDate,
                new HashSet<>()
        );
        Tag tag = new Tag("Tag" + i);
        User user = new User("user" + i, "user" + i + "@domain.com", new HashSet<>());
        Order order = new Order(i);

        certificate.assignTag(tag);
        order.addCertificate(certificate);
        order.setUser(user);

        giftCertificateRepository.save(certificate);
        userRepository.save(user);
        user.placeOrder(order);
        userRepository.save(user);
    }

    private void addCertificate(int i) {
        String localDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        GiftCertificate certificate = new GiftCertificate(
                "Certificate" + i,
                "Description" + i,
                BigDecimal.valueOf(10.00),
                10,
                localDate,
                localDate,
                new HashSet<>()
        );
        giftCertificateRepository.save(certificate);
    }
}
